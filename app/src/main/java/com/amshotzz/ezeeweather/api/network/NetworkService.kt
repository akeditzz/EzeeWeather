package com.amshotzz.ezeeweather.api.network

import com.amshotzz.ezeeweather.main.dataClass.WeatherDataClass
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {
    @GET("weather")
    fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") openWheatherKey: String
    ): Single<WeatherDataClass>
}