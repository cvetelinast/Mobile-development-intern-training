package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class CitiesLocalDataSource private constructor(private val cityDao: CityDao) : CityDataSource, LocalDataSource {

    override fun getCities(): Single<List<CityEntity>> = cityDao.getAll()
        .subscribeOn(Schedulers.single())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.single())


    override fun getCity(cityName: String): Single<CityEntity> = cityDao.getCity(cityName)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.single())


    override fun addCity(cityEntity: CityEntity): Single<CityEntity> =
        Single.fromCallable {
            cityDao.insertCity(cityEntity)
            cityEntity
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.single())


    override fun refreshCity(newCity: CityEntity): Single<CityEntity> =
        Single.fromCallable {
            cityDao.updateCity(newCity.name, newCity.lastTemperature, newCity.lastImageId)

            val cityEntity = CityEntity()
            cityEntity.name = newCity.name
            cityEntity.lastTemperature = newCity.lastTemperature
            cityEntity.lastImageId = newCity.lastImageId

            cityEntity
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.single())


    override fun deleteCity(cityName: String): Single<String> = Single.fromCallable {
        cityDao.deleteCity(cityName)
        cityName
    }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.single())


    companion object {

        private var INSTANCE: CitiesLocalDataSource? = null

        fun getInstance(cityDao: CityDao): CitiesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(CitiesLocalDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CitiesLocalDataSource(cityDao)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
