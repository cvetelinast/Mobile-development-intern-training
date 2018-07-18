package com.example.tsvetelinastoyanova.weatherapp.data.source

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single


interface CityDataSource {

    interface GetCityCallback {

        fun onCityLoaded(cityEntity: CityEntity)

        fun onCityDoesNotExist()
    }

    fun getCity(cityName: String) : Single<CityEntity>

    interface AddCityCallback {

        fun onCityAddedSuccessfully(cityEntity: CityEntity)

        fun onFail()
    }

    interface GetWeatherObjectCallback {

        fun onWeatherObjectLoaded(weatherObject: CurrentWeatherObject)

        fun onFail()
    }

}