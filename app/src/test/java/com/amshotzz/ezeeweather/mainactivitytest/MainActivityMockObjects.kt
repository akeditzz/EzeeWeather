package com.amshotzz.ezeeweather.mainactivitytest

import com.amshotzz.ezeeweather.database.entity.WeatherEntity
import java.net.ConnectException

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
val errorMessage = "Oops!! Something went wrong"
fun getErrorResponse(): Throwable {
    val errorResponse = Throwable(errorMessage)
    errorResponse.addSuppressed(ConnectException())
    return errorResponse
}