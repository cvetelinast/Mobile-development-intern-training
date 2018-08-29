package com.example.tsvetelinastoyanova.hackernewsapp.data

import android.arch.paging.PagedList
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class StoriesRemoteRepository : StoriesRepository {

    private val pagedListTopNews: PagedList<Story>? = null
    private val pagedListLastNews: PagedList<Story>? = null

    companion object {
        private var INSTANCE: StoriesRemoteRepository? = null
        fun getInstance(): StoriesRemoteRepository {
            if (INSTANCE == null) {
                synchronized(StoriesRemoteRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoriesRemoteRepository()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    fun loadTopNews(storiesListObservable: Observable<PagedList<Story>>): Observable<PagedList<Story>> {
        return storiesListObservable.doOnNext { storiesList ->
            pagedListTopNews?.addAll(storiesList)
        }
    }

    fun loadLastNews(storiesListObservable: Observable<PagedList<Story>>): Observable<PagedList<Story>> {
        return storiesListObservable.doOnNext { storiesList ->
            pagedListLastNews?.addAll(storiesList)
        }
    }
}