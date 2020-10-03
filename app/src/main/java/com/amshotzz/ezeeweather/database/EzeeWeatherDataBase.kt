package com.amshotzz.ezeeweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amshotzz.ezeeweather.database.dao.EzeeWeatherDao
import com.amshotzz.ezeeweather.database.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class EzeeWeatherDataBase : RoomDatabase() {
    companion object {
        @Volatile
        private var sInstance: EzeeWeatherDataBase? = null

        @Synchronized
        fun getInstance(context: Context): EzeeWeatherDataBase? {
            if (sInstance == null) {
                synchronized(EzeeWeatherDataBase::class.java) {
                    if (sInstance == null) {
                        sInstance = Room.databaseBuilder(
                            context,
                            EzeeWeatherDataBase::class.java,
                            "EzeeWeatherDataBase"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return sInstance
        }
    }

    abstract fun ezeeWeatherDao(): EzeeWeatherDao?
}