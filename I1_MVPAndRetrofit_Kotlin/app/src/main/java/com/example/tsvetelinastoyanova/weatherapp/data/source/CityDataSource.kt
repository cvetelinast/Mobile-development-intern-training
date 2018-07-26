package com.example.tsvetelinastoyanova.weatherapp.data.source

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import io.reactivex.Single

interface CityDataSource {

    interface GetCityCallback {

        fun onCityLoaded(cityEntity: CityEntity)

        fun onCityDoesNotExist()
    }

    fun getCity(cityName: String): Single<CityEntity>
}