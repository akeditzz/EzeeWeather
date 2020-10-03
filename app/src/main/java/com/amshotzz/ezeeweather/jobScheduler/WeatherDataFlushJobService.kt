package com.amshotzz.ezeeweather.jobScheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.AsyncTask
import com.amshotzz.ezeeweather.database.EzeeWeatherDataRepository
import timber.log.Timber

class WeatherDataFlushJobService() : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        val weatherDataRepository: EzeeWeatherDataRepository? =
            EzeeWeatherDataRepository.getInstance(this)
        deleteWeatherEntityIntoDatabaseAsynstask(
            params?.jobId ?: -1,
            weatherDataRepository
        ).execute()
        return true
    }

    inner class deleteWeatherEntityIntoDatabaseAsynstask(
        var jobId: Int,
        var weatherDataRepository: EzeeWeatherDataRepository?
    ) :
        AsyncTask<Unit, Unit, String>() {
        override fun doInBackground(vararg params: Unit?): String {
            weatherDataRepository?.deleteWeatherEntity(jobid = jobId)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Timber.i("Data Flushed")
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}