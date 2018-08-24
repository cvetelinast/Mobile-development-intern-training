package com.example.tsvetelinastoyanova.hackernewsapp.data

import com.example.tsvetelinastoyanova.hackernewsapp.model.StoriesList
import io.reactivex.Observable

interface Repository {

    fun getTopStories(): Observable<List<Int>>
}