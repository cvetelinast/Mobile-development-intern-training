package com.example.tsvetelinastoyanova.weatherapp

interface Constants {
    companion object {
        val DEGREES_CELSIUS = "%s °C"
        val IMG_URL = "http://openweathermap.org/img/w/"
        val WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=956b0cec24dc9a86dddab6ed85a14dd5"
        val WEATHER_MULTIPLE_CITIES_URL = "http://api.openweathermap.org/data/2.5/group?id=%s&APPID=956b0cec24dc9a86dddab6ed85a14dd5"
        val FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s,%s&APPID=956b0cec24dc9a86dddab6ed85a14dd5"
        val COUNTRY_CITY = "%s, %s"
        val WEATHER_OBJECTS = "weatherObjects"
        val API_KEY = "956b0cec24dc9a86dddab6ed85a14dd5"
        val DATABASE_NAME = "cities"

        // fragments names
        val WEATHER_DETAILS_FRAGMENT = "Weather details"
        val FORECAST_FRAGMENT = "Forecast"
    }
}
