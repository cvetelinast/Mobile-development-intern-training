package com.example.tsvetelinastoyanova.weatherapp.util

import com.example.tsvetelinastoyanova.weatherapp.R


object ImageOperator {

    fun getImageIdFromString(idString: String): Int {
        var id = 0
        when (idString) {
            "01d" -> id = R.drawable.sun
            "02d" -> id = R.drawable.sun_cloud
            "03d", "03n" -> id = R.drawable.cloud
            "04d", "04n" -> id = R.drawable.clouds
            "09d", "09n" -> id = R.drawable.clouds_rain
            "10d" -> id = R.drawable.cloud_sun_rain
            "11d", "11n" -> id = R.drawable.storm
            "13d", "13n" -> id = R.drawable.snow
            "50d", "50n" -> id = R.drawable.mist
            "01n" -> id = R.drawable.brown
            "02n" -> id = R.drawable.cloud_brown
            "10n" -> id = R.drawable.cloud_moon_rain
            else -> id = R.drawable.ic_launcher_background
        }
        return id
    }
}
