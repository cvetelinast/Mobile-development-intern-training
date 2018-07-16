package com.example.tsvetelinastoyanova.weatherapp.data.source

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject


interface CityDataSource {

    interface GetCityCallback {

        fun onCityLoaded(cityEntity: CityEntity)

        fun onCityDoesNotExist()
    }

    fun getCity(cityName: String, callback: GetCityCallback)

    interface AddCityCallback {

        fun onCityAddedSuccessfully(cityEntity: CityEntity)

        fun onFail()
    }

    interface GetWeatherObjectCallback {

        fun onWeatherObjectLoaded(weatherObject: CurrentWeatherObject)

        fun onFail()
    }

}