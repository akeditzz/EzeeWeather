package com.amshotzz.ezeeweather.mvvmBase

import android.app.Activity
import android.app.job.JobScheduler
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.test.espresso.IdlingResource
import com.amshotzz.ezeeweather.utils.common.LoadingDialog
import com.amshotzz.ezeeweather.utils.common.SimpleIdlingResource


abstract class BaseActivity : AppCompatActivity() {


    lateinit var viewModel: BaseViewModel

    private var mIdlingResource: SimpleIdlingResource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = setUpViewModel()
        setDataBindingLayout()
        setupObservers()
        setupView(savedInstanceState)
        viewModel.onCreate()
    }

    protected open fun setupObservers() {
        viewModel.messageString.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.showDialog.observe(this, Observer {
            showLoadingDialog(it)
        })
    }

    private fun showLoadingDialog(it: Boolean) {
        if (it) {
            LoadingDialog.showDialog()
        } else {
            LoadingDialog.dismissDialog()
        }
    }

    private fun showMessage(message: String) = show(applicationContext, message)

    private fun show(context: Context, text: CharSequence) {
        val toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun isJobServiceOn(JOB_ID: Int): Boolean {
        val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        var hasBeenScheduled = false
        for (jobInfo in scheduler.allPendingJobs) {
            if (jobInfo.id == JOB_ID) {
                hasBeenScheduled = true
                break
            }
        }
        return hasBeenScheduled
    }

    /**
     * Only called from test, creates and returns a new [SimpleIdlingResource].
     */
    @VisibleForTesting
    open fun getIdlingResource(): IdlingResource? {
        if (mIdlingResource == null) {
            mIdlingResource = SimpleIdlingResource()
        }
        return mIdlingResource
    }

    protected abstract fun setDataBindingLayout()

    protected abstract fun setupView(savedInstanceState: Bundle?)

    protected abstract fun setUpViewModel(): BaseViewModel

}