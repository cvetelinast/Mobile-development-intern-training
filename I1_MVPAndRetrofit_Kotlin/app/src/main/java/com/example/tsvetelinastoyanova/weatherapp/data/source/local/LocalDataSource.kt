package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import io.reactivex.Flowable
import io.reactivex.Single


interface LocalDataSource : CityDataSource {

    interface AddCityCallback {

        fun onCityAddedSuccessfully(cityEntity: CityEntity)

        fun onCityExistsInDatabase()

        fun onNotValidCity()
    }

    interface DeleteCityCallback {

        fun onCityDeletedSuccessfully()

        fun onFail()
    }

    fun getCities(): Single<List<CityEntity>>

    fun addCity(cityEntity: CityEntity): Single<CityEntity>

    fun refreshCity(cityEntity: CityEntity): Single<CityEntity>

    fun deleteCity(cityName: String): Single<String>
}
