package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface GetDataService {

    @GET("topstories.json")
    fun getTopStories(): Observable<List<Int>>

    @GET("newstories.json")
    fun getNewStories(): Observable<List<Int>>

    @GET("item/{id}.json")
    fun getStoryById(@Path("id") path: String): Observable<Story?>

    @GET("maxitem.json")
    fun getMaxItemId(): Single<Int>
}