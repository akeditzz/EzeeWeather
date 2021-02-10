package com.amshotzz.ezeeweather.database

import android.content.Context
import com.amshotzz.ezeeweather.database.dao.EzeeWeatherDao
import com.amshotzz.ezeeweather.database.entity.WeatherEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class EzeeWeatherDataRepository(
    var mDao: EzeeWeatherDao? = null,
    var mIoExecutor: ExecutorService? = null
) {

    companion object {
        @Volatile
        private var sInstance: EzeeWeatherDataRepository? = null

        /**
         * Function to provide instance of data repository
         */
        fun getInstance(context:Context): EzeeWeatherDataRepository? {
            if (sInstance == null) {
                synchronized(EzeeWeatherDataRepository::class.java) {
                    if (sInstance == null) {
                        val database: EzeeWeatherDataBase? =
                            EzeeWeatherDataBase.getInstance(context)
                        sInstance = EzeeWeatherDataRepository(
                            database?.ezeeWeatherDao(),
                            Executors.newSingleThreadExecutor()
                        )
                    }
                }
            }
            return sInstance
        }
    }

    /**
     * function to call insert method of DAO
     */
    fun insertWeatherEntity(weatherEntity: WeatherEntity) {
        mDao?.insertWeatherEntity(weatherEntity)
    }

    /**
     * function to call get weather data method of DAO
     */
    fun getWeatherEntity(cityname: String): WeatherEntity? {
        return mDao?.getWeatherByCityName(cityname.toLowerCase().capitalize())
    }

    /**
     * function to call delete weather data method of DAO
     */
    fun deleteWeatherEntity(jobid: Int) {
        mIoExecutor?.execute { mDao?.deleteWeatherData(jobid = jobid) }
    }

}