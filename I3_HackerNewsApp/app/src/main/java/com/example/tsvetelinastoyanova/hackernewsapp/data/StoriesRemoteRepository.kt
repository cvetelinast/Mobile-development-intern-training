package com.example.tsvetelinastoyanova.hackernewsapp.data

import android.arch.paging.PagedList
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.new.NewStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.top.TopStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class StoriesRemoteRepository(private val topStoriesRemoteDataSource: TopStoriesRemoteDataSource,
                              private val newStoriesRemoteDataSource: NewStoriesRemoteDataSource) : StoriesRepository {

//    private val pagedListTopNews: PagedList<Story>? = null  //- not mutable list because it's not possible to be merged
//    private val pagedListLastNews: MutableList<Story> = mutableListOf()

    companion object {

        //    private val pagedListTopNews: PagedList<Story>? = null
        //    private val pagedListLastNews: PagedList<Story>? = null

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
       // Log.d("size", "Size of pagedListTopNews: ${pagedListTopNews?.size}")
      /*  if (pagedListTopNews != null) {
            return Observable.just(pagedListTopNews)
                .mergeWith(storiesListObservable)
                .doOnNext { storiesList ->
                    pagedListTopNews.addAll(storiesList)
                }
        }*/
        return storiesListObservable
         /*   .doOnNext { storiesList ->
                pagedListTopNews?.addAll(storiesList)
                //   storiesList
            }*/
    }

    fun loadLastNews(storiesListObservable: Observable<PagedList<Story>>): Observable<PagedList<Story>> {
        return storiesListObservable
           /* .doOnNext { storiesList ->
            pagedListLastNews?.addAll(storiesList)
        }*/
    }
}