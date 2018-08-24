package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import com.example.tsvetelinastoyanova.hackernewsapp.model.StoriesList
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface GetDataService {

    @GET("topstories.json")
    fun getTopStories(): Observable<List<Int>>

    @GET("newstories")
    fun getNewStories(): Observable<List<Int>>
}