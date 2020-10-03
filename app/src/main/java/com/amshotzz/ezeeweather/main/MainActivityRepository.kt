package com.amshotzz.ezeeweather.main

import com.amshotzz.ezeeweather.BuildConfig
import com.amshotzz.ezeeweather.api.network.NetworkService
import com.amshotzz.ezeeweather.database.EzeeWeatherDataRepository
import com.amshotzz.ezeeweather.database.entity.WeatherEntity
import com.amshotzz.ezeeweather.main.dataClass.WeatherDataClass
import io.reactivex.Single
import javax.inject.Inject

class MainActivityRepository @Inject constructor(
    val networkService: NetworkService,
    val ezeeWeatherDataRepository: EzeeWeatherDataRepository?
) {
    /**
     * Function to call network methods to get weather data from api
     */
    fun getWeatherByCityName(cityName: String): Single<WeatherDataClass> = networkService.getWeatherByCityName(cityName,BuildConfig.OpenWheatherKey)

    /**
     * Function to call db methods to insert weather data
     */
    fun insertWeatherEntityIntoDatabase(weatherEntity: WeatherEntity) {
        ezeeWeatherDataRepository?.insertWeatherEntity(weatherEntity)
    }

    /**
     * Function to call db methods to get weather data from db
     */
    fun getWeatherEntityFromDatabase(cityName: String): WeatherEntity? = ezeeWeatherDataRepository?.getWeatherEntity(cityName)
}