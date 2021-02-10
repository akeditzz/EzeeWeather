package com.amshotzz.ezeeweather.main

import android.os.AsyncTask
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.amshotzz.ezeeweather.BuildConfig
import com.amshotzz.ezeeweather.R
import com.amshotzz.ezeeweather.database.entity.WeatherEntity
import com.amshotzz.ezeeweather.main.dataClass.WeatherDataClass
import com.amshotzz.ezeeweather.mvvmBase.BaseViewModel
import com.amshotzz.ezeeweather.utils.common.Resource
import com.amshotzz.ezeeweather.utils.network.NetworkHelper
import com.amshotzz.ezeeweather.utils.rx.SchedulerProvider
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivityViewModel @ViewModelInject constructor(
    ezVSchedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    ezVNetworkHelper: NetworkHelper,
    private val mainActivityRepository: MainActivityRepository
) : BaseViewModel(ezVSchedulerProvider, compositeDisposable, ezVNetworkHelper) {
    override fun onCreate() {
    }

    companion object {
        /**
         * Function to load image via glide
         */
        @JvmStatic
        @BindingAdapter("icon")
        fun loadImage(view: ImageView, icon: String?) {
            if (!icon.isNullOrEmpty()) {
                Glide.with(view.context).load("${BuildConfig.Icon_Base_Url}${icon}.png")
                    .fitCenter().placeholder(R.drawable.ic_sun).into(view)
            }
        }
    }

    // Live Data variables
    val weatherLiveData = MutableLiveData<WeatherEntity>()
    val temperature = MutableLiveData<CharSequence>()
    val temperatureDesc = MutableLiveData<CharSequence>()
    val sunrise = MutableLiveData<CharSequence>()
    val sunset = MutableLiveData<CharSequence>()
    val pressure = MutableLiveData<CharSequence>()
    val humidity = MutableLiveData<CharSequence>()
    val windSpeed = MutableLiveData<CharSequence>()
    val windDegree = MutableLiveData<CharSequence>()
    val generateJobIdLiveData = MutableLiveData<WeatherDataClass>()
    var jobId: Int = -1

    /**
     * Initiating api call to get weather details using cityname
     */
    fun getWeatherByCityName(cityName: String) {

        if (checkInternetConnectionWithMessage()) {
            compositeDisposable.addAll(
                mainActivityRepository.getWeatherByCityName(cityName)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        {
                            showDialog.postValue(false)
                            generateJobIdLiveData.postValue(it)
                        }, {
                            showDialog.postValue(false)
                            messageString.postValue(Resource.error("Oops!! Something went wrong"))
                        }
                    ))
        }

    }

    /**
     * Asynctask class to get weather details from local database if exists else get from api
     */
    inner class getWeatherEntityFromDatabaseAsynstask(var cityName: String) :
        AsyncTask<Unit, Unit, WeatherEntity?>() {
        override fun doInBackground(vararg params: Unit?): WeatherEntity? {
            return mainActivityRepository.getWeatherEntityFromDatabase(cityName)
        }

        override fun onPostExecute(result: WeatherEntity?) {
            super.onPostExecute(result)
            if (result != null) {
                weatherLiveData.postValue(result)
            } else {
                getWeatherByCityName(cityName)
            }
        }

    }

    /**
     * Function to create entity object and insert data into local db
     */
    fun getWeatherEntityData(it: WeatherDataClass?): WeatherEntity? {
        val weatherEntity = WeatherEntity(
            jobId,
            it?.name ?: "",
            it?.weather?.get(0)?.icon ?: "",
            it?.weather?.get(0)?.main ?: "",
            it?.main?.temp ?: 0.0,
            it?.main?.tempMin ?: 0.0,
            it?.main?.humidity ?: 0,
            it?.main?.pressure ?: 0,
            it?.main?.tempMax ?: 0.0,
            it?.sys?.sunrise ?: 0,
            it?.sys?.sunset ?: 0,
            it?.wind?.deg ?: 0,
            it?.wind?.speed ?: 0.0
        )

        insertWeatherEntityIntoDatabaseAsynstask(weatherEntity).execute() // inserting data to local db
        return weatherEntity
    }

    /**
     * AsyncTask call to insert data into local db
     */
    inner class insertWeatherEntityIntoDatabaseAsynstask(var weatherEntity: WeatherEntity) :
        AsyncTask<Unit, Unit, String>() {
        override fun doInBackground(vararg params: Unit?): String {
            mainActivityRepository.insertWeatherEntityIntoDatabase(weatherEntity)
            return ""
        }
    }

    /**
     * Function to add subscript text to a Charsequence
     */
    fun getSubScriptTextForDegree(source: String): CharSequence? {
        val mSource = "${source}o"
        val index: Int = mSource.indexOf("o")
        val cs = SpannableStringBuilder(mSource)
        cs.setSpan(SuperscriptSpan(), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        cs.setSpan(RelativeSizeSpan(0.75f), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return cs
    }

    /**
     * Function to get date or time in specified format
     */
    fun getDateOrTimeFromSpecificFormat(
        format: String,
        date: Long
    ): String? {
        return try {

            val sdf1 = SimpleDateFormat(format)
            sdf1.timeZone = TimeZone.getDefault()
            sdf1.format(Date(date).time)
        } catch (e: ParseException) {
            Timber.e(e)
            ""
        }
    }

    /**
     * Function for activity to initiate insertion of data to local db and set value for ui
     */
    fun insertAndSetWeatherData(it: WeatherDataClass?) {
        weatherLiveData.postValue(getWeatherEntityData(it))
    }

    /**
     * Setting the values for livedata variables
     */
    fun setLiveData(it: WeatherEntity) {
        temperature.value =
            getSubScriptTextForDegree(
                (it.temp - 272.15).roundToInt().toString()
            )

        temperatureDesc.value = TextUtils.concat(
            it.main,
            " ",
            getSubScriptTextForDegree(
                (it.tempMin - 272.15).roundToInt().toString()
            ),
            "C / ",
            getSubScriptTextForDegree(
                (it.tempMax - 272.15).roundToInt().toString()
            ),
            "C"
        )

        sunrise.value = "Sunrise ${
            getDateOrTimeFromSpecificFormat(
                "HH:mm",
                it.sunrise.toLong()
            )
        }"
        sunset.value = "Sunset ${
            getDateOrTimeFromSpecificFormat(
                "HH:mm",
                it.sunset.toLong()
            )
        }"

        pressure.value = "Pressure ${it.pressure} hpa"
        humidity.value = "Humidity ${it.humidity}%"
        windSpeed.value = "Wind Speed ${it.speed} km/h"
        windDegree.value = TextUtils.concat(
            "Wind Degree ",
            getSubScriptTextForDegree(it.deg.toString())
        )
    }

}