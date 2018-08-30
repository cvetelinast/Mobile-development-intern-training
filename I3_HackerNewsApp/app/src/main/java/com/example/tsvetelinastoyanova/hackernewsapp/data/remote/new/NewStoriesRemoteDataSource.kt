package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.new

import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.GetDataService
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.RetrofitClient
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class NewStoriesRemoteDataSource : AbstractDataSource()/*ItemKeyedDataSource<Long, Story>()*/ {
    private val ids: MutableList<Int> = mutableListOf()

  var lastReceivedIndex: Int = 0
  private val storiesList: MutableList<Story> = mutableListOf()
    companion object {
        private var INSTANCE: NewStoriesRemoteDataSource? = null
        fun getInstance(): NewStoriesRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(NewStoriesRemoteDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = NewStoriesRemoteDataSource()
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

  /*  override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Story>) {
        if(ids.isEmpty()) {
            getNewStoriesIds()
                .flatMapIterable { ids -> this.ids.addAll(ids);ids }
                .take(13)
                .flatMap { id -> getStoryById(id.toString()) }
                .toList()
                .subscribe(
                    {                        stories -> callback.onResult(stories)
                    },
                    {
                        error -> Log.d("tag", "Error in loadInitial(): $error")
                    }
                )
        } else {
            Observable.fromIterable(ids)
                .flatMap { id -> getStoryById(id.toString()) }
                .toList()
                .subscribe(
                    {
                        stories -> callback.onResult(stories)
                    },
                    {
                        error -> Log.d("tag", "Error in loadInitial(): $error")
                    }
                )
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        val storiesCountToBeLoaded: Int
        if (lastReceivedIndex + 10 < ids.size) {
            lastReceivedIndex += 10
            storiesCountToBeLoaded = 10
        } else {
            storiesCountToBeLoaded = ids.size - lastReceivedIndex
        }
        val sublist = ids.subList(lastReceivedIndex, lastReceivedIndex + storiesCountToBeLoaded)

        Observable.fromIterable(sublist)
            .flatMap { id -> getStoryById(id.toString()) }
            .toList()
            .subscribe(
                { stories -> callback.onResult(stories) },
                { error -> Log.d("tag", "Error in loadAfter(): $error") }
            )
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
        return service.getNewStories()
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

/*
    private fun getStoryById(id: String): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getStoryById(id)
    }*/
}
