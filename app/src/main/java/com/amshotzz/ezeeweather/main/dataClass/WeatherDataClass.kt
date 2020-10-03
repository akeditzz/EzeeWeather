package com.amshotzz.ezeeweather.main.dataClass

import com.fasterxml.jackson.annotation.JsonProperty
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherDataClass(

	@field:JsonProperty("dt")
	val dt: Int? = null,

	@field:JsonProperty("coord")
	val coord: Coord? = null,

	@field:JsonProperty("visibility")
	val visibility: Int? = null,

	@field:JsonProperty("weather")
	val weather: List<WeatherItem?>? = null,

	@field:JsonProperty("name")
	val name: String? = null,

	@field:JsonProperty("cod")
	val cod: Int? = null,

	@field:JsonProperty("main")
	val main: Main? = null,

	@field:JsonProperty("clouds")
	val clouds: Clouds? = null,

	@field:JsonProperty("id")
	val id: Int? = null,

	@field:JsonProperty("timezone")
	val timezone: Int? = null,

	@field:JsonProperty("sys")
	val sys: Sys? = null,

	@field:JsonProperty("base")
	val base: String? = null,

	@field:JsonProperty("wind")
	val wind: Wind? = null,

	@field:JsonProperty("jobId")
	val jobId: Int? = null
) : Parcelable

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Clouds(

	@field:JsonProperty("all")
	val all: Int? = null
) : Parcelable

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Coord(

	@field:JsonProperty("lon")
	val lon: Double? = null,

	@field:JsonProperty("lat")
	val lat: Double? = null
) : Parcelable

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Wind(

	@field:JsonProperty("deg")
	val deg: Int? = null,

	@field:JsonProperty("speed")
	val speed: Double? = null
) : Parcelable

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Sys(

	@field:JsonProperty("country")
	val country: String? = null,

	@field:JsonProperty("sunrise")
	val sunrise: Int? = null,

	@field:JsonProperty("sunset")
	val sunset: Int? = null,

	@field:JsonProperty("id")
	val id: Int? = null,

	@field:JsonProperty("type")
	val type: Int? = null,

	@field:JsonProperty("message")
	val message: Double? = null
) : Parcelable

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Main(

	@field:JsonProperty("temp")
	val temp: Double? = null,

	@field:JsonProperty("temp_min")
	val tempMin: Double? = null,

	@field:JsonProperty("humidity")
	val humidity: Int? = null,

	@field:JsonProperty("pressure")
	val pressure: Int? = null,

	@field:JsonProperty("temp_max")
	val tempMax: Double? = null
) : Parcelable

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherItem(

	@field:JsonProperty("icon")
	val icon: String? = null,

	@field:JsonProperty("description")
	val description: String? = null,

	@field:JsonProperty("main")
	val main: String? = null,


	@field:JsonProperty("id")
	val id: Int? = null
) : Parcelable
