package com.example.tsvetelinastoyanova.weatherapp.data.source

import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.CitiesLocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.remote.CitiesRemoteDataSource
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import com.example.tsvetelinastoyanova.weatherapp.util.CityEntityAdapter
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import java.util.ArrayList


class CitiesRepository private constructor(localDataSource: LocalDataSource,
                                           remoteDataSource: CitiesRemoteDataSource) : CityDataSource {
    private val localDataSource: LocalDataSource = checkNotNull(localDataSource)
    private val remoteDataSource: CitiesRemoteDataSource = checkNotNull(remoteDataSource)

    internal var weatherObjectsCache: MutableList<CurrentWeatherObject> = ArrayList()
    internal var forecastObjectsCache: MutableList<ForecastObject> = ArrayList()

    fun getCities(getCityCallback: CityDataSource.GetCityCallback) {
        /*
        if dirty cache:
        1) getAll cities from DB
                1.1) success - forEach cityEntity request to the API
                    1.1.1) success - return list of cityEntities
                    1.1.2) fail - error with internet
                1.2) fail - error
         else:
         2) getAll cities from DB
                2.1) success - return cityEntities from DB one by one
                2.2) fail - error
         */
        Utils.checkNotNull(getCityCallback)
        if (!weatherObjectsCache.isEmpty()) {
            getCitiesFromCache(getCityCallback)
        } else {
            getCitiesFromDataSource(getCityCallback)
        }
    }

    private fun getCitiesFromDataSource(getCityCallback: CityDataSource.GetCityCallback) {
        localDataSource.getCities(object : LocalDataSource.LoadCitiesCallback {
            override fun onCitiesLoaded(cities: List<CityEntity>) {
                if (!cities.isEmpty()) {
                    for (cityEntity in cities) {
                        cityEntity.name?.let {
                            remoteDataSource.getWeatherObject(it, object : CityDataSource.GetWeatherObjectCallback {
                                override fun onWeatherObjectLoaded(weatherObject: CurrentWeatherObject) {
                                    refreshWeatherObjectInCache(weatherObject)
                                    getCityCallback.onCityLoaded(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject))
                                }

                                override fun onFail() {
                                    getCityCallback.onCityDoesNotExist()
                                }
                            })
                        }
                    }
                }
            }

            override fun onDataNotAvailable() {
                getCityCallback.onCityDoesNotExist()
            }
        })
    }

    private fun getCitiesFromCache(getCityCallback: CityDataSource.GetCityCallback) {
        synchronized(weatherObjectsCache) {
            for (weatherObject in weatherObjectsCache) {
                val cityEntity = CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject)
                getCityCallback.onCityLoaded(cityEntity)
            }
        }
    }

    override fun getCity(cityName: String, callback: CityDataSource.GetCityCallback) {
        Utils.checkNotNull(cityName)
        Utils.checkNotNull(callback)

        localDataSource.getCity(cityName, object : CityDataSource.GetCityCallback {
            override fun onCityLoaded(city: CityEntity) {
                callback.onCityLoaded(city)
            }

            override fun onCityDoesNotExist() {
                callback.onCityDoesNotExist()
            }
        })
    }

    fun addCity(cityName: String, callback: LocalDataSource.AddCityCallback) {
        /* 1) check if city exists in database
            1.1) exists - onCityAlreadyExists()
            1.2) does not exist - api request()
                1.2.1) success
                    1.2.1.1) write to DB
                        1.2.1.1.1) success - return message
                        1.2.1.1.2) fail - return message
                1.2.2) fail - return message */

        Utils.checkNotNull(cityName)
        Utils.checkNotNull(callback)

        localDataSource.getCity(cityName, object : CityDataSource.GetCityCallback {
            override fun onCityDoesNotExist() {
                remoteDataSource.getWeatherObject(cityName, object : CityDataSource.GetWeatherObjectCallback {
                    override fun onWeatherObjectLoaded(weatherObject: CurrentWeatherObject) {
                        localDataSource.addCity(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject), object : LocalDataSource.AddCityCallback {
                            override fun onCityAddedSuccessfully(cityEntity: CityEntity) {
                                refreshWeatherObjectInCache(weatherObject)
                                callback.onCityAddedSuccessfully(cityEntity)
                            }

                            override fun onFail() {
                                callback.onFail()
                            }
                        })
                    }

                    override fun onFail() {
                        callback.onFail()
                    }
                })
            }

            override fun onCityLoaded(city: CityEntity) {
                callback.onFail()
            }
        })
    }

    fun deleteCity(cityName: String, deleteCityCallback: LocalDataSource.DeleteCityCallback) {
        Utils.checkNotNull(cityName)
        localDataSource.deleteCity(cityName, object : LocalDataSource.DeleteCityCallback {
            override fun onCityDeletedSuccessfully() {
                deleteWeatherObjectFromCache(cityName)
                deleteCityCallback.onCityDeletedSuccessfully()
            }

            override fun onFail() {
                deleteCityCallback.onFail()
            }
        })
    }

    fun getWeatherObjectWithName(cityName: String): CurrentWeatherObject? {
        synchronized(weatherObjectsCache) {
            val saveIndex: Int
            for (weatherObject in weatherObjectsCache) {
                weatherObject.let {
                    if (it.name.equals(cityName)) {
                        return weatherObject
                    }
                }
            }
            saveIndex = weatherObjectsCache.size

            addWeatherObjectToCacheFromRemote(cityName)

            if (saveIndex > weatherObjectsCache.size) {
                for (i in saveIndex - 1 until weatherObjectsCache.size) {
                    if (weatherObjectsCache[i].name.equals(cityName)) {
                        return weatherObjectsCache[i]
                    }
                }
            }
        }
        return null
    }

    fun refreshCities(cities: List<City>, getCityCallback: CityDataSource.GetCityCallback) {
        /* foreach city
            load from DB
            get name and load from API
            refresh cache
            refresh DB
            refresh UI
         */
        for (c in cities) {
            getCity(c.name, object : CityDataSource.GetCityCallback {
                override fun onCityLoaded(oldCity: CityEntity) {
                    remoteDataSource.getWeatherObject(oldCity.name, object : CityDataSource.GetWeatherObjectCallback {
                        override fun onWeatherObjectLoaded(weatherObject: CurrentWeatherObject) {
                            refreshWeatherObjectInCache(weatherObject)
                            val newCity = CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject)
                            localDataSource.refreshCity(newCity, LocalDataSource.RefreshCityCallback {
                                getCityCallback.onCityLoaded(newCity)
                            })
                        }

                        override fun onFail() {
                            getCityCallback.onCityDoesNotExist()
                        }
                    })
                }

                override fun onCityDoesNotExist() {
                    getCityCallback.onCityDoesNotExist()
                }
            })
        }
    }

    fun refreshWeatherObjectInCache(newObject: CurrentWeatherObject) {
        synchronized(this) {
            for (i in weatherObjectsCache.indices) {
                if (weatherObjectsCache[i].name.equals(newObject.name)) {
                    weatherObjectsCache[i] = newObject
                    return
                }
            }

            weatherObjectsCache.add(newObject)
        }
    }

    private fun deleteWeatherObjectFromCache(cityName: String) {
        synchronized(weatherObjectsCache) {
            for (weatherObject in weatherObjectsCache) {
                if (weatherObject.name.equals(cityName)) {
                    weatherObjectsCache.remove(weatherObject)
                    return
                }
            }
        }
    }

    private fun addWeatherObjectToCacheFromRemote(cityName: String) {
        synchronized(weatherObjectsCache) {
            remoteDataSource.getWeatherObject(cityName, object : CityDataSource.GetWeatherObjectCallback {
                override fun onWeatherObjectLoaded(weatherObject: CurrentWeatherObject) {
                    weatherObjectsCache.add(weatherObject)
                }

                override fun onFail() {

                }
            })
        }
    }

    companion object {
        private var INSTANCE: CitiesRepository? = null

        fun getInstance(localDataSource: LocalDataSource,
                        remoteDataSource: CitiesRemoteDataSource): CitiesRepository {
            if (INSTANCE == null) {
                INSTANCE = CitiesRepository(localDataSource, remoteDataSource)
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}