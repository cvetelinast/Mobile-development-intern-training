package com.example.tsvetelinastoyanova.weatherapp.model.currentweather

import com.google.gson.annotations.SerializedName

data class Wind(
        @SerializedName("speed") val speed: Double,
        @SerializedName("deg") val deg: Int
)