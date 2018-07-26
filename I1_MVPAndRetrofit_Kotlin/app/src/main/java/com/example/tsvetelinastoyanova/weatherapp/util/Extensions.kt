package com.example.tsvetelinastoyanova.weatherapp.util

import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Main

fun CurrentWeatherObject.convertToWeatherObjectWithCelsiusTemperature(): CurrentWeatherObject {
    val main = Main(temp = this.main.temp.convertTemperatureToCelsius(), pressure = this.main.pressure,
        humidity = this.main.humidity, tempMin = this.main.tempMin.convertTemperatureToCelsius(),
        tempMax = this.main.tempMax.convertTemperatureToCelsius())

    return CurrentWeatherObject(coord = this.coord, weather = this.weather, base = this.base,
        main = main, visibility = this.visibility, wind = this.wind, clouds = this.clouds,
        dt = this.dt, sys = this.sys, id = this.id, name = this.name, cod = this.cod)
}

fun Double.convertTemperatureToCelsius(): Double {
    return Math.round((this - 273.15) * 10) / 10.0
}