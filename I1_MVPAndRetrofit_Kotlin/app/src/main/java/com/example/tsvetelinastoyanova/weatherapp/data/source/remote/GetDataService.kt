package com.example.tsvetelinastoyanova.weatherapp.data.source.remote

import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {

    @GET("weather")
    fun getWeatherForCity(@Query("APPID") appId: String, @Query("q", encoded = false) city: String): Single<CurrentWeatherObject>

    @GET("forecast")
    fun getForecastForCity(@Query("APPID") appId: String, @Query("q", encoded = false) city: String): Single<ForecastObject>
}