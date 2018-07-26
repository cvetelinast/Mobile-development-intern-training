package com.example.tsvetelinastoyanova.weatherapp.util

import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject

fun convertCityEntityToCity(cityEntity: CityEntity): City {
    return City(cityEntity.name, cityEntity.lastTemperature, cityEntity.lastImageId)
}

fun convertWeatherObjectToCityEntity(weatherObjectsList: CurrentWeatherObject): CityEntity {
    val c = CityEntity()
    c.name = weatherObjectsList.name
    c.cityId = weatherObjectsList.id
    c.lastTemperature = weatherObjectsList.main.temp
    val imageId = weatherObjectsList.weather[0].icon
    c.lastImageId = ImageOperator.getImageIdFromString(imageId)
    return c
}


