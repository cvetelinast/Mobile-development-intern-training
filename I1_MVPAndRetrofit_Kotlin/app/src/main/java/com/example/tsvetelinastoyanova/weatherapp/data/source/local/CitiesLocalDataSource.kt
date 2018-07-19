package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CitiesLocalDataSource private constructor(private val cityDao: CityDao) : CityDataSource, LocalDataSource {

    override fun getCities(): Flowable<List<CityEntity>> {
        return cityDao.getAll().subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
    }

    override fun getCity(cityName: String): Single<CityEntity> {
        Utils.checkNotNull(cityName)
        return cityDao.getCity(cityName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
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
    }

    override fun deleteCity(cityName: String): Single<String> {
        Utils.checkNotNull(cityName)

        return Single.fromCallable {
            cityDao.deleteCity(cityName)
            cityName
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
    }

    companion object {

        private var INSTANCE: CitiesLocalDataSource? = null

        fun getInstance(cityDao: CityDao): CitiesLocalDataSource? {
            if (INSTANCE == null) {
                synchronized(CitiesLocalDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CitiesLocalDataSource(cityDao)
                    }
                }
            }
            return INSTANCE
        }
    }
}
