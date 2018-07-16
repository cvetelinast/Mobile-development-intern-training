package com.example.tsvetelinastoyanova.weatherapp.util

import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import java.util.ArrayList

object CityEntityAdapter {

    fun convertCityEnitiesToCities(cityEntities: List<CityEntity>): List<City> {
        val cities = ArrayList<City>()
        for (cityEntity in cityEntities) {
            cities.add(City(cityEntity.name, cityEntity.lastTemperature, cityEntity.lastImageId))
        }
        return cities
    }

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

    fun convertFirstWeatherObjectToCityEntity(weatherObjectsList: ForecastObject): CityEntity {
        val c = CityEntity()
        c.name = weatherObjectsList.city.name
        c.cityId = weatherObjectsList.city.id
        c.lastTemperature = weatherObjectsList.list[0].main.temp
        val imageId = weatherObjectsList.list[0].weather[0].icon
        c.lastImageId = ImageOperator.getImageIdFromString(imageId)
        return c
    }

    fun convertWeatherObjectToCity(weatherObject: ForecastObject): City {
        val image = weatherObject.list[0].weather[0].icon
        return City(weatherObject.city.name, weatherObject.list[0].main.temp, ImageOperator.getImageIdFromString(image))
    }

    fun reduceWeatherObjectsList(weatherObjectsList: ForecastObject): ForecastObject {
        return ForecastObject(cod = weatherObjectsList.cod, message = weatherObjectsList.message,
                city = weatherObjectsList.city, cnt = weatherObjectsList.cnt, list = weatherObjectsList.list.subList(0, 10))
    }
}
