package com.example.tsvetelinastoyanova.hackernewsapp.data

import android.arch.paging.PagedList
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesdatasources.NewStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesdatasources.TopStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class StoriesRemoteRepository(private val topStoriesRemoteDataSource: TopStoriesRemoteDataSource,
                              private val newStoriesRemoteDataSource: NewStoriesRemoteDataSource) : StoriesRepository {

    companion object {

        private var INSTANCE: StoriesRemoteRepository? = null
        fun getInstance(topStoriesRemoteDataSource: TopStoriesRemoteDataSource, newStoriesRemoteDataSource: NewStoriesRemoteDataSource): StoriesRemoteRepository {
            if (INSTANCE == null) {
                synchronized(StoriesRemoteRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoriesRemoteRepository(topStoriesRemoteDataSource, newStoriesRemoteDataSource)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    fun loadTopNews(storiesListObservable: Observable<PagedList<Story>>): Observable<PagedList<Story>> {
        return storiesListObservable
    }

    fun loadLastNews(storiesListObservable: Observable<PagedList<Story>>): Observable<PagedList<Story>> {
        return storiesListObservable
    }
}