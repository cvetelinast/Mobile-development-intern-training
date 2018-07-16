package com.example.tsvetelinastoyanova.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class WeatherObject(
        @SerializedName("dt") val dt: Int,
        @SerializedName("main") val main: Main,
        @SerializedName("weather") val weather: List<Weather>,
        @SerializedName("clouds") val clouds: Clouds,
        @SerializedName("wind") val wind: Wind,
        @SerializedName("sys") val sys: Sys,
        @SerializedName("dt_txt") val dtTxt: String
)