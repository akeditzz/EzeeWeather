package com.amshotzz.ezeeweather.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amshotzz.ezeeweather.database.entity.WeatherEntity

@Dao
interface EzeeWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherEntity(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather_data WHERE name=:cityName")
    fun getWeatherByCityName(cityName: String?): WeatherEntity

    @Query("Delete FROM weather_data WHERE jobid=:jobid")
    fun deleteWeatherData(jobid: Int?)
}
