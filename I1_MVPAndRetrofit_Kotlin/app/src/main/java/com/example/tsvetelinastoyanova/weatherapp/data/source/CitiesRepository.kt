package com.example.tsvetelinastoyanova.weatherapp.data.source

import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.remote.CitiesRemoteDataSource
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import com.example.tsvetelinastoyanova.weatherapp.util.CityEntityAdapter
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class CitiesRepository private constructor(localDataSource: LocalDataSource,
                                           remoteDataSource: CitiesRemoteDataSource) : CityDataSource {
    private val localDataSource: LocalDataSource = checkNotNull(localDataSource)
    private val remoteDataSource: CitiesRemoteDataSource = checkNotNull(remoteDataSource)

    internal var weatherObjectsCache: MutableList<CurrentWeatherObject> = ArrayList()
    internal var forecastObjectsCache: MutableList<ForecastObject> = ArrayList()


    private val disposables = CompositeDisposable()

    companion object {
        @Volatile
        private var INSTANCE: CitiesRepository? = null

        fun getInstance(localDataSource: LocalDataSource,
                        remoteDataSource: CitiesRemoteDataSource): CitiesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                        ?: CitiesRepository(localDataSource, remoteDataSource).also { INSTANCE = it }
            }
        }
    }

    fun getCities(getCityCallback: CityDataSource.GetCityCallback) {
        /*
        if have cache
        load cities from cache
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
        localDataSource.getCities()
                .doOnError { error -> getCityCallback.onCityDoesNotExist() }
                .flatMap { listOfCityEntities -> Flowable.fromIterable(listOfCityEntities) }
                .flatMap { cityEntity: CityEntity -> remoteDataSource.getWeatherObject(cityEntity.name).toFlowable() }
                .doOnError { error -> getCityCallback.onCityDoesNotExist() }
                .map { weatherObject -> Utils.convertToWeatherObjectWithCelsiusTemperature(weatherObject) }
                .doOnNext { currentWeatherObject ->
                    refreshWeatherObjectInCache(currentWeatherObject)
                    getCityCallback.onCityLoaded(CityEntityAdapter.convertWeatherObjectToCityEntity(currentWeatherObject))
                }
                .doOnError { error -> getCityCallback.onCityDoesNotExist() }
                .subscribe()


        /*   localDataSource.getCities(
           object : LocalDataSource.LoadCitiesCallback {
               override fun onCitiesLoaded(cities: List<CityEntity>) {
                   if (!cities.isEmpty()) {
                       for (cityEntity in cities) {
                           cityEntity.name?.let {
                               val weatherObjectSingle = remoteDataSource.getWeatherObject(it)
                               val disposable = weatherObjectSingle
                                       .subscribe(
                                               { weatherObject ->
                                                   refreshWeatherObjectInCache(weatherObject)
                                                   getCityCallback.onCityLoaded(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject)) // trqbva da vleze tyk, no ne vliza
                                               },
                                               { error -> getCityCallback.onCityDoesNotExist() })
                           }
                       }
                   }
               }

               override fun onDataNotAvailable() {
                   getCityCallback.onCityDoesNotExist()
               }
           })*/
    }

    private fun getCitiesFromCache(getCityCallback: CityDataSource.GetCityCallback) {
        synchronized(weatherObjectsCache) {
            for (weatherObject in weatherObjectsCache) {
                val cityEntity = CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject)
                getCityCallback.onCityLoaded(cityEntity)
            }
        }
    }

    override fun getCity(cityName: String): Single<CityEntity> {
        Utils.checkNotNull(cityName)

        return localDataSource.getCity(cityName)
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

        // OLD:
        /*
         localDataSource.getCity(cityName, object : CityDataSource.GetCityCallback {
            override fun onCityDoesNotExist(){
                remoteDataSource.getWeatherObject(cityName, object : CityDataSource.GetWeatherObjectCallback {
            override fun onWeatherObjectLoaded(weatherObject: CurrentWeatherObject) {
            // todo: tyk

            //       val observable: Observable<CityEntity> = localDataSource.addCity(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject))
            localDataSource.addCity(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject),
                    object : LocalDataSource.AddCityCallback {
                        override fun onCityAddedSuccessfully(cityEntity: CityEntity) {
                            refreshWeatherObjectInCache(weatherObject)
                            callback.onCityAddedSuccessfully(cityEntity)
                        }

                        override fun onCityExistsInDatabase() {
                            callback.onCityExistsInDatabase()
                        }
                    })
            *//* observable.subscribe(
                     { value ->
                         refreshWeatherObjectInCache(weatherObject)
                         callback.onCityAddedSuccessfully(value)
                     },
                     { err -> callback.onCityExistsInDatabase() }
             )*//*
        }

        override fun onCityExistsInDatabase() {
            callback.onCityExistsInDatabase()
        }
    })
    }*/

        /*  override fun onCityLoaded(city: CityEntity) {
        callback.onCityExistsInDatabase()
        }
        })*/

        // NEW:
        val getCitySingle = localDataSource.getCity(cityName)
        val s = remoteDataSource.getWeatherObject(cityName)
                .map { weatherObject -> Utils.convertToWeatherObjectWithCelsiusTemperature(weatherObject) }
                .flatMap { weatherObject ->
                    localDataSource.addCity(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject))
                            .map { cityEntity: CityEntity -> cityEntity to weatherObject }
                }.doOnSuccess {
                    refreshWeatherObjectInCache(it.second)
                    callback.onCityAddedSuccessfully(it.first)
                }.map { it.first }

        val disposable = getCitySingle
                .doOnSuccess { callback.onCityExistsInDatabase() }
                .onErrorResumeNext(s)
                .subscribe()

        disposables.add(disposable)
    }

/*
.subscribe(
      { cityEntity ->},
      { callback.onCityExistsInDatabase()}
)}
*/

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
            for (weatherObject in weatherObjectsCache) {
                weatherObject.let {
                    if (it.name == cityName) {
                        return weatherObject
                    }
                }
            }
            val saveIndex = weatherObjectsCache.size
            addWeatherObjectToCacheFromRemote(cityName)

            if (saveIndex > weatherObjectsCache.size) {
                for (i in saveIndex - 1 until weatherObjectsCache.size) {
                    if (weatherObjectsCache[i].name == cityName) {
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
            val observable: Single<CityEntity> = getCity(c.name)
            observable.subscribe(
                    { oldCity ->
                        remoteDataSource.getWeatherObject(oldCity.name)
                                .subscribe(
                                        { weatherObject -> val currentWeatherObject = Utils.convertToWeatherObjectWithCelsiusTemperature(weatherObject)
                                            refreshWeatherObjectInCache(currentWeatherObject)
                                            val newCity = CityEntityAdapter.convertWeatherObjectToCityEntity(currentWeatherObject)
                                            /*  localDataSource.refreshCity(newCity, LocalDataSource.RefreshCityCallback {
                                              getCityCallback.onCityLoaded(newCity) }) */
                                        },
                                        { error -> getCityCallback.onCityDoesNotExist() }
                                )
                    },
                    { err -> getCityCallback.onCityDoesNotExist() }
            )
        }
    }

    fun refreshWeatherObjectInCache(newObject: CurrentWeatherObject) {
        synchronized(this) {
            for (i in weatherObjectsCache.indices) {
                if (weatherObjectsCache[i].name == newObject.name) {
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
                if (weatherObject.name == cityName) {
                    weatherObjectsCache.remove(weatherObject)
                    return
                }
            }
        }
    }

    private fun addWeatherObjectToCacheFromRemote(cityName: String) {
        synchronized(weatherObjectsCache) {
            remoteDataSource.getWeatherObject(cityName)
                    .subscribe(
                            { weatherObject -> weatherObjectsCache.add(weatherObject) }
                    )
        }
    }
}