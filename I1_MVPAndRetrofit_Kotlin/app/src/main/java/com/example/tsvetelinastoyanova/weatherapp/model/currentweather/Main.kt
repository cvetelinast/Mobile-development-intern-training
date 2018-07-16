package com.example.tsvetelinastoyanova.weatherapp.model.currentweather

import com.google.gson.annotations.SerializedName

data class Main(
        @SerializedName("temp") val temp: Double,
        @SerializedName("pressure") val pressure: Int,
        @SerializedName("humidity") val humidity: Int,
        @SerializedName("temp_min") val tempMin: Double,
        @SerializedName("temp_max") val tempMax: Double
)