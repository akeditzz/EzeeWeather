package com.amshotzz.ezeeweather.dbtest

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amshotzz.ezeeweather.database.EzeeWeatherDataBase
import com.amshotzz.ezeeweather.database.entity.WeatherEntity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EzeeWeatherDataBaseTest {
    private lateinit var weatherDataBase: EzeeWeatherDataBase
    val weatherEntity = WeatherEntity(
        1,
        "Pune",
        "01a",
        "Cloudy",
        100.0,
        100.0,
        100,
        1000,
        100.0,
        1600000,
        1600000,
        270,
        10.0
    )

    @Before
    fun initDb() {
        weatherDataBase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            EzeeWeatherDataBase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        weatherDataBase.close()
    }

    @Test
    fun insertWeatherData() {
        weatherDataBase.ezeeWeatherDao()?.insertWeatherEntity(weatherEntity)
        val result = weatherDataBase.ezeeWeatherDao()?.getWeatherByCityName("Pune")
        assertEquals(weatherEntity, result)
    }

    @Test
    fun deleteWeatherData() {
        weatherDataBase.ezeeWeatherDao()?.deleteWeatherData(weatherEntity.jobid)
        val result = weatherDataBase.ezeeWeatherDao()?.getWeatherByCityName("Pune")
        assertEquals(null, result)
    }

}