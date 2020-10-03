package com.amshotzz.ezeeweather.main

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.amshotzz.ezeeweather.R
import com.amshotzz.ezeeweather.databinding.ActivityMainBinding
import com.amshotzz.ezeeweather.di.components.ActivityComponent
import com.amshotzz.ezeeweather.jobScheduler.WeatherDataFlushJobService
import com.amshotzz.ezeeweather.mvvmBase.BaseActivity
import com.amshotzz.ezeeweather.utils.common.Resource


class MainActivity : BaseActivity<MainActivityViewModel>() {
    private var mMenu: Menu? = null
    val MINIMUM_LATENCY = 86400000 //24 hours
    var dataBinding: ActivityMainBinding? = null


    override fun setDataBindingLayout() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding?.mainActivityViewModel = viewModel
        dataBinding?.lifecycleOwner = this
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_sun)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dataBinding?.let {
            with(it, {

                // Textwatcher in edittext to validate cityname should not be empty
                etSearchCity.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        btnSearch.isEnabled = s.toString()
                            .isNotEmpty() //enable / disable button depending on editext text value
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }

                })

                // changing the background of search layout depending on editext text focus
                etSearchCity.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        searchLayout.setBackgroundResource(R.drawable.accent_cornered_rectangle_stroke)
                    } else {
                        searchLayout.setBackgroundResource(R.drawable.white_cornered_rectangle_stroke)
                    }
                }

                // action listener for ime search option key
                etSearchCity.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        getWeatherByCityName()
                        true
                    } else false
                })

                btnSearch.setOnClickListener {
                    getWeatherByCityName()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObservers() {
        super.setupObservers()
        viewModel.weatherLiveData.observe(this, Observer {
            // if data is available load it and hide/show search layout and hide/show search icon in menu so that user can again search if needed.
            if (it != null) {
                mMenu?.findItem(R.id.action_search)?.isVisible = true
                dataBinding?.searchLayout?.visibility = View.GONE
                dataBinding?.bodyScrollLayout?.visibility = View.VISIBLE
                viewModel.setLiveData(it)
            } else {
                dataBinding?.searchLayout?.visibility = View.VISIBLE
                dataBinding?.bodyScrollLayout?.visibility = View.GONE
                mMenu?.findItem(R.id.action_search)?.isVisible = false
            }
        })

        viewModel.generateJobIdLiveData.observe(this, Observer {
            // Generating random jobid for jobscheduler
            viewModel.jobId = (1..10000).random()
            while (isJobServiceOn(viewModel.jobId)) { // checking to see if the job id is already scheduled
                viewModel.jobId = (1..10000).random()
            }
            viewModel.insertAndSetWeatherData(it) // inserting the data to local storage (Room DB) and setting values to live data
            scheduleJobToFlush(viewModel.jobId) // schedule a job to flush the local stored data after 24 hours
        })
    }

    /**
     * Function to schedule a job to flush the local stored data after 24 hours
     */
    private fun scheduleJobToFlush(jobId: Int) {
        val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceComponent = ComponentName(this, WeatherDataFlushJobService::class.java)
        val builder = JobInfo.Builder(
            jobId,
            serviceComponent
        )
        builder.setMinimumLatency(MINIMUM_LATENCY.toLong()) // wait at least
        scheduler.schedule(builder.build())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        mMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            dataBinding?.searchLayout?.visibility = View.VISIBLE
            item.isVisible = false
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Function to validate and call api to get weather details
     */
    private fun getWeatherByCityName() {
        hideKeyboard()
        if (dataBinding?.etSearchCity?.text?.isNotEmpty() == true) {
            viewModel.getWeatherEntityFromDatabaseAsynstask(dataBinding?.etSearchCity?.text.toString())
                .execute()
        } else {
            viewModel.messageString.postValue(Resource.error(getString(R.string.mesg_please_enter_city_name)))
        }
    }

}