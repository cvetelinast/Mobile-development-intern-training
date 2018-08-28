package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.google.gson.GsonBuilder

object RetrofitClient {

    private const val BASE_URL_HACKER_NEWS = "https://hacker-news.firebaseio.com/v0/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: Retrofit by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL_HACKER_NEWS)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}