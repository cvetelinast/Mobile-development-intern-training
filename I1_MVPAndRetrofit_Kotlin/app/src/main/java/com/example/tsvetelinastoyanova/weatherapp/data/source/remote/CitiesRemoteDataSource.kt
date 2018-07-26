package com.example.tsvetelinastoyanova.weatherapp.data.source.remote

import com.example.tsvetelinastoyanova.weatherapp.Constants
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CitiesRemoteDataSource {
    companion object {

        private var INSTANCE: CitiesRemoteDataSource? = null

        fun getInstance(): CitiesRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(CitiesRemoteDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CitiesRemoteDataSource()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    fun getWeatherObject(city: String): Single<CurrentWeatherObject> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getWeatherForCity(Constants.API_KEY, city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getForecastForCity(city: String): Single<ForecastObject> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getForecastForCity(Constants.API_KEY, city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}