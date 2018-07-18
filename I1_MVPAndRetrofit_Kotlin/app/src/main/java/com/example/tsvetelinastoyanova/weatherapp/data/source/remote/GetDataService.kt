package com.example.tsvetelinastoyanova.weatherapp.data.source.remote

import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {

    @GET("weather")
    fun getWeatherForCity(@Query("APPID") appId: String, @Query("q") city: String): Single<CurrentWeatherObject>
}