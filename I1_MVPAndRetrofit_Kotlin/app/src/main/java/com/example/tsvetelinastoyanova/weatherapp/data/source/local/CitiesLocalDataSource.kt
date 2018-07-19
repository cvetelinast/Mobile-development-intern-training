package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import com.example.tsvetelinastoyanova.weatherapp.util.AppExecutors
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CitiesLocalDataSource private constructor(private val mAppExecutors: AppExecutors,
                                                private val cityDao: CityDao) : CityDataSource, LocalDataSource {

    override fun getCities(): Flowable<List<CityEntity>> {
        return cityDao.getAll().subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getCity(cityName: String): Single<CityEntity> {
        Utils.checkNotNull(cityName)
        return cityDao.getCity(cityName)
                .observeOn(Schedulers.single())
    }

    override fun addCity(cityEntity: CityEntity): Single<CityEntity> {
        Utils.checkNotNull(cityEntity)
        return Single.fromCallable {
            cityDao.insertCity(cityEntity)
            cityEntity
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
    }

    override fun refreshCity(newCity: CityEntity): Single<CityEntity> {
        Utils.checkNotNull(newCity)
        return Single.fromCallable {
            cityDao.updateCity(newCity.name, newCity.lastTemperature, newCity.lastImageId)

            val cityEntity = CityEntity()
            cityEntity.name = newCity.name; cityEntity.lastTemperature = newCity.lastTemperature; cityEntity.lastImageId = newCity.lastImageId
            cityEntity
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())

        /* val runnable = lambda@{
             newCity.name?.let {
                 cityDao.updateCity(it, newCity.lastTemperature, newCity.lastImageId)
                 mAppExecutors.mainThread().execute({ refreshCityCallback.onRefreshCitySuccessfully() })
             }
             return@lambda
         }

         mAppExecutors.databaseIO().execute(runnable)*/
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
