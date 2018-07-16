package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import android.content.Context
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import com.example.tsvetelinastoyanova.weatherapp.util.AppExecutors
import com.example.tsvetelinastoyanova.weatherapp.util.Utils

class CitiesLocalDataSource private constructor(private val mAppExecutors: AppExecutors,
                                                private val cityDao: CityDao) : CityDataSource, LocalDataSource {

    override fun getCities(callback: LocalDataSource.LoadCitiesCallback) {
        Utils.checkNotNull(callback)
        val runnable = {
            val cities = cityDao.all
            mAppExecutors.mainThread().execute({
                if (cities.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onCitiesLoaded(cities)
                }
            })
        }

        mAppExecutors.databaseIO().execute(runnable)
    }

    override fun getCity(cityName: String, callback: CityDataSource.GetCityCallback) {
        Utils.checkNotNull(cityName)
        Utils.checkNotNull(callback)
        val runnable = {
            val city = cityDao.getCity(cityName)
            mAppExecutors.mainThread().execute({

                if (city == null) {
                    callback.onCityDoesNotExist()
                } else {
                    callback.onCityLoaded(city)
                }
            })
        }
        mAppExecutors.databaseIO().execute(runnable)
    }

    override fun addCity(cityEntity: CityEntity, addCityCallback: LocalDataSource.AddCityCallback) {
        Utils.checkNotNull(cityEntity)
        Utils.checkNotNull(addCityCallback)
        val runnable = {
            cityDao.insertCity(cityEntity)
            mAppExecutors.mainThread().execute({ addCityCallback.onCityAddedSuccessfully(cityEntity) })
        }
        mAppExecutors.databaseIO().execute(runnable)
    }

    override fun refreshCity(newCity: CityEntity, refreshCityCallback: LocalDataSource.RefreshCityCallback) {
        Utils.checkNotNull(newCity)
        Utils.checkNotNull(refreshCityCallback)
        val runnable = lambda@{

            newCity.name?.let {
                cityDao.updateCity(it, newCity.lastTemperature, newCity.lastImageId)
                mAppExecutors.mainThread().execute({ refreshCityCallback.onRefreshCitySuccessfully() })
            }
            return@lambda
        }
        mAppExecutors.databaseIO().execute(runnable)
    }

    override fun deleteCity(cityName: String, callback: LocalDataSource.DeleteCityCallback) {
        Utils.checkNotNull(cityName)
        Utils.checkNotNull(callback)
        val runnable = {
            cityDao.deleteCity(cityName)
            mAppExecutors.mainThread().execute({ callback.onCityDeletedSuccessfully() })
        }
        mAppExecutors.databaseIO().execute(runnable)
    }

    companion object {

        private var INSTANCE: CitiesLocalDataSource? = null

        fun getInstance(appExecutors: AppExecutors,
                        cityDao: CityDao): CitiesLocalDataSource? {
            if (INSTANCE == null) {
                synchronized(CitiesLocalDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CitiesLocalDataSource(appExecutors, cityDao)
                    }
                }
            }
            return INSTANCE
        }
    }
}
