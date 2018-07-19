package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


interface LocalDataSource : CityDataSource {
    interface LoadCitiesCallback {

        fun onCitiesLoaded(cities: List<CityEntity>)

        fun onDataNotAvailable()

    }

    interface AddCityCallback {

        fun onCityAddedSuccessfully(cityEntity: CityEntity)

        fun onCityExistsInDatabase()

        fun onNotValidCity()
    }

    interface DeleteCityCallback {

        fun onCityDeletedSuccessfully()

        fun onFail()
    }

    interface RefreshCityCallback {

        fun onRefreshCitySuccessfully()

    }

    fun getCities(): Flowable<List<CityEntity>>

    fun addCity(cityEntity: CityEntity): Single<CityEntity>

    fun refreshCity(cityEntity: CityEntity): Single<CityEntity>

    fun deleteCity(cityName: String): Single<String>
}
