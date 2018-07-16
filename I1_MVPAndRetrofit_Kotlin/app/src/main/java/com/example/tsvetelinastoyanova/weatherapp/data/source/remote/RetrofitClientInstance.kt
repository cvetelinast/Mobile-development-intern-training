package com.example.tsvetelinastoyanova.weatherapp.data.source.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClientInstance {
    const val BASE_URL_CITY = "http://api.openweathermap.org/data/2.5/"
    val instance: Retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL_CITY)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}