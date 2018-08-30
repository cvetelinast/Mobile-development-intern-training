package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.top

import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.GetDataService
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.RetrofitClient
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.new.AbstractDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class TopStoriesRemoteDataSource : AbstractDataSource()/*ItemKeyedDataSource<Long, Story>()*/ {

    private val ids: MutableList<Int> = mutableListOf()
    private val storiesList: MutableList<Story> = mutableListOf()
    var lastReceivedIndex: Int = 0

    companion object {
        private const val STORIES_NUMBER_PER_PAGE: Int = 13

        private var INSTANCE: TopStoriesRemoteDataSource? = null
        fun getInstance(): TopStoriesRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(TopStoriesRemoteDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = TopStoriesRemoteDataSource()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun addStoriesToList(storiesList: List<Story>) {
        this.storiesList.addAll(storiesList)
    }

    override fun getStoriesList(): List<Story> {
        return storiesList
    }

    override fun getLastIndex(): Int {
        return lastReceivedIndex
    }

    override fun increaseLastReceivedIndexWithValue(value: Int) {
        lastReceivedIndex += value
    }

    override fun getIdsSize(): Int {
       return ids.size
    }

    override fun addIdsFirstTime(listIds: List<Int>) {
        ids.addAll(listIds)
    }

    override fun getSublistOfCachedStories(fromIndex: Int, toIndex: Int): MutableList<Int> {
        return ids.subList(fromIndex, toIndex)
    }

    /* override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Story>) {
        if (isFirstTimeLoadingStories()) {
            getTopStoriesIds()
                .flatMapIterable { idList -> ids.addAll(idList);idList }
                .take(STORIES_NUMBER_PER_PAGE.toLong())
                .flatMap { id -> getStoryById(id.toString()) }
                .toList()
                .subscribe(
                    { stories ->
                        topStoriesList.addAll(stories)
                        lastReceivedIndex += lastReceivedIndex + stories.size
                        callback.onResult(stories)
                    },
                    { error ->
                        Log.d("tag", "Error in loadInitial(): $error")
                    }
                )
        } else {
            Observable.just(topStoriesList)
                .subscribe(
                    { stories ->
                        callback.onResult(stories)
                    },
                    { error ->
                        Log.d("tag", "Error in loadInitial(): $error")
                    }
                )
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        synchronized(this) {
            val storiesCountToBeLoaded: Int
            if (lastReceivedIndex + STORIES_NUMBER_PER_PAGE < ids.size) {
                lastReceivedIndex += STORIES_NUMBER_PER_PAGE
                storiesCountToBeLoaded = STORIES_NUMBER_PER_PAGE
            } else {
                storiesCountToBeLoaded = ids.size - lastReceivedIndex
            }
            val sublist = ids.subList(lastReceivedIndex, lastReceivedIndex + storiesCountToBeLoaded)

            Observable.fromIterable(sublist)
                .flatMap { id -> getStoryById(id.toString()) }
                .toList()
                .subscribe(
                    { stories ->
                        topStoriesList.addAll(stories)
                        callback.onResult(stories)
                    },
                    { error -> Log.d("tag", "Error in loadAfter(): $error") }
                )
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        Log.d("tag", "loadBefore() called")
    }

    override fun getKey(item: Story): Long {
        return item.id.toLong()
    }
*/
    override fun getStoriesIds(): Observable<List<Int>> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getTopStories()
    }
/*
    private fun getLastStoriesIds(): Observable<List<Int>> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getNewStories()
    }

    private fun isFirstTimeLoadingStories() = lastReceivedIndex == 0

    *//* private fun getTopStories(): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getTopStories()
            .flatMapIterable { ids -> ids }
            .flatMap { id -> getStoryById(id.toString()) }
    }
    *//*
    private fun getStoryById(id: String): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getStoryById(id)
    }*/
}
