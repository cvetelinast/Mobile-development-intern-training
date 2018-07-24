package com.example.tsvetelinastoyanova.weatherapp.util

import com.example.tsvetelinastoyanova.weatherapp.R


object ImageOperator {

    fun getImageIdFromString(idString: String): Int {
        return when (idString) {
            "01d" -> R.drawable.sun
            "02d" -> R.drawable.sun_cloud
            "03d", "03n" -> R.drawable.cloud
            "04d", "04n" -> R.drawable.clouds
            "09d", "09n" -> R.drawable.clouds_rain
            "10d" -> R.drawable.cloud_sun_rain
            "11d", "11n" -> R.drawable.storm
            "13d", "13n" -> R.drawable.snow
            "50d", "50n" -> R.drawable.mist
            "01n" -> R.drawable.brown
            "02n" -> R.drawable.cloud_brown
            "10n" -> R.drawable.cloud_moon_rain
            else -> R.drawable.ic_launcher_background
        }
    }
}
