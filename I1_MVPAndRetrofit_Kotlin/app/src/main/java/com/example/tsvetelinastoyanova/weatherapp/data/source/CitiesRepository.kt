package com.example.tsvetelinastoyanova.weatherapp.data.source

import android.util.Log
import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.remote.CitiesRemoteDataSource
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import com.example.tsvetelinastoyanova.weatherapp.util.convertWeatherObjectToCityEntity
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


sealed class CityState
object ExistsInDatabase : CityState()
object NotValid : CityState()
data class AddedSuccessfully(val currentWeatherObject: CurrentWeatherObject, val cityEntity: CityEntity) : CityState()


class CitiesRepository private constructor(localDataSource: LocalDataSource,
                                           remoteDataSource: CitiesRemoteDataSource) : CityDataSource {
    private val localDataSource: LocalDataSource = checkNotNull(localDataSource)
    private val remoteDataSource: CitiesRemoteDataSource = checkNotNull(remoteDataSource)

    private var weatherObjectsCache: MutableList<CurrentWeatherObject> = ArrayList()
    private var forecastObjectsCache: MutableList<ForecastObject> = ArrayList()

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
        if (!weatherObjectsCache.isEmpty()) {
            getCitiesFromCache(getCityCallback)
        } else {
            getCitiesFromDataSource(getCityCallback)
        }
    }

    private fun getCitiesFromDataSource(getCityCallback: CityDataSource.GetCityCallback) {
        localDataSource.getCities()
            .flatMapPublisher { listOfCityEntities -> Flowable.fromIterable(listOfCityEntities) }
            .flatMapSingle { cityEntity: CityEntity -> remoteDataSource.getWeatherObject(cityEntity.name) }
            .map { weatherObject -> Utils.convertToWeatherObjectWithCelsiusTemperature(weatherObject) }
            .subscribe(
                { currentWeatherObject ->
                    refreshWeatherObjectInCache(currentWeatherObject)
                    getCityCallback.onCityLoaded(convertWeatherObjectToCityEntity(currentWeatherObject))
                },
                { error -> getCityCallback.onCityDoesNotExist() }
            )
    }

    private fun getCitiesFromCache(getCityCallback: CityDataSource.GetCityCallback) {
        synchronized(weatherObjectsCache) {
            for (weatherObject in weatherObjectsCache) {
                val cityEntity = convertWeatherObjectToCityEntity(weatherObject)
                getCityCallback.onCityLoaded(cityEntity)
            }
        }
    }

    override fun getCity(cityName: String): Single<CityEntity> {
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

        val onErrorRecoverFlowable: Single<CityState> = remoteDataSource.getWeatherObject(cityName)
            .map { weatherObject -> Utils.convertToWeatherObjectWithCelsiusTemperature(weatherObject) }
            .flatMap { weatherObject -> addCityInLocalDataSource(weatherObject) }
            .map { (cityEntity, currentWeatherObject) -> AddedSuccessfully(currentWeatherObject, cityEntity) }

        localDataSource.getCity(cityName)
            .map<CityState> { ExistsInDatabase }
            .onErrorResumeNext(onErrorRecoverFlowable)
            .subscribe(
                { result ->
                    if (result is AddedSuccessfully) {
                        refreshWeatherObjectInCache(result.currentWeatherObject)
                        callback.onCityAddedSuccessfully(result.cityEntity)
                    }
                },
                { error -> callback.onNotValidCity() }
            )
    }

    private fun addCityInLocalDataSource(weatherObject: CurrentWeatherObject) =
        localDataSource.addCity(convertWeatherObjectToCityEntity(weatherObject))
            .map { cityEntity: CityEntity -> cityEntity to weatherObject }


    fun deleteCity(cityName: String, deleteCityCallback: LocalDataSource.DeleteCityCallback) {
        localDataSource.deleteCity(cityName)
            .doOnError { error -> deleteCityCallback.onFail() }
            .map { delete -> deleteWeatherObjectFromCache(delete) }
            .subscribe(
                { _ -> deleteCityCallback.onCityDeletedSuccessfully() },
                { error -> deleteCityCallback.onFail() }
            )
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
        /*** foreach city
         * load from DB
         * get name and load from API
         * refresh cache
         * refresh DB
         *refresh UI
         ***/
        Flowable.fromIterable(cities)
            .flatMapSingle { city -> getCity(city.name) }
            .doOnError { error -> getCityCallback.onCityDoesNotExist() }
            .flatMapSingle { city -> remoteDataSource.getWeatherObject(city.name) }
            .doOnError { error -> getCityCallback.onCityDoesNotExist() }
            .map { weatherObject -> Utils.convertToWeatherObjectWithCelsiusTemperature(weatherObject) }
            .map { weatherObject ->
                refreshWeatherObjectInCache(weatherObject)
                weatherObject
            }
            .map { newCity -> convertWeatherObjectToCityEntity(newCity) }
            .flatMapSingle { newCity -> localDataSource.refreshCity(newCity) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { success -> getCityCallback.onCityLoaded(success) },
                { error -> getCityCallback.onCityDoesNotExist() }
            )
    }

    fun getForecastForCity(city: String): Single<ForecastObject> {
        /***
         * 1) If cache
         *      load from cache
         * 2) Else
         *      load from remote data source
         *      save in cache
         ***/
        if (!forecastObjectsCache.isEmpty()) {
            return getForecastFromCache(city)
        } else {
            val result = getForecastFromDataSource(city)
            return result
        }
    }

    private fun getForecastFromCache(city: String): Single<ForecastObject> {
        val forecastObject = forecastObjectsCache.find { s -> s.city.name == city }
        forecastObject ?: return getForecastFromDataSource(city)
        return Single.just(forecastObject)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    private fun getForecastFromDataSource(city: String): Single<ForecastObject> {
        return remoteDataSource.getForecastForCity(city)
            .map { newObject ->
                Log.d("tag", "New object $newObject")
                addForecastObjectInCache(newObject)
                newObject
            }
            .doOnSuccess { value -> Log.d("tag", "Value: $value") }
            .doOnError { error -> Log.d("tag", "Error: $error") }
    }

    private fun addForecastObjectInCache(newObject: ForecastObject) {
        synchronized(this) {
            forecastObjectsCache.add(newObject)
        }
    }

    private fun refreshWeatherObjectInCache(newObject: CurrentWeatherObject) {
        synchronized(this) {
            var exists = false
            loop@ for (i in weatherObjectsCache.indices) {
                if (weatherObjectsCache[i].name == newObject.name) {
                    weatherObjectsCache[i] = newObject
                    exists = true
                    break@loop
                }
            }
            if (!exists) {
                weatherObjectsCache.add(newObject)
            }
        }
    }

    private fun deleteWeatherObjectFromCache(cityName: String) {
        synchronized(weatherObjectsCache) {
            loop@ for (weatherObject in weatherObjectsCache) {
                if (weatherObject.name == cityName) {
                    weatherObjectsCache.remove(weatherObject)
                    break@loop
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