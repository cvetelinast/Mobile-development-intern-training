package com.example.tsvetelinastoyanova.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastObject(
    @SerializedName("cod") val cod: String,
    @SerializedName("message") val message: Double,
    @SerializedName("cnt") val cnt: Int,
    @SerializedName("list") val list: List<WeatherObject>,
    @SerializedName("city") val city: City
)