package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource


interface LocalDataSource : CityDataSource {
    interface LoadCitiesCallback {

        fun onCitiesLoaded(cities: List<CityEntity>)

        fun onDataNotAvailable()

    }

    interface AddCityCallback {

        fun onCityAddedSuccessfully(cityEntity: CityEntity)

        fun onFail()
    }

    interface DeleteCityCallback {

        fun onCityDeletedSuccessfully()

        fun onFail()
    }

    interface RefreshCityCallback {

        fun onRefreshCitySuccessfully()

    }

    fun getCities(callback: LoadCitiesCallback)

    fun addCity(cityEntity: CityEntity, addCityCallback: AddCityCallback)

    fun refreshCity(cityEntity: CityEntity, refreshCityCallback: LocalDataSource.RefreshCityCallback)

    fun deleteCity(cityName: String, callback: LocalDataSource.DeleteCityCallback)
}
