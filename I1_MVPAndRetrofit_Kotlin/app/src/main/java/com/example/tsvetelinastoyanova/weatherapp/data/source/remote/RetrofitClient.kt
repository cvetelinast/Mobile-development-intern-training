package com.example.tsvetelinastoyanova.weatherapp.data.source.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitClient {
    public val instance: Retrofit by lazy {
        retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL_CITY)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private const val BASE_URL_CITY = "http://api.openweathermap.org/data/2.5/"
}