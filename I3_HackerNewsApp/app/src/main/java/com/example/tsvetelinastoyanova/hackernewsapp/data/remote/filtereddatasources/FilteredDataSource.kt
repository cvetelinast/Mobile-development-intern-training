package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.filtereddatasources

import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.common.Utils.NUM_STORIES_FOR_PAGE
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.GetDataService
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.RetrofitClient
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FilteredDataSource(private val searchedString: String) : ItemKeyedDataSource<Long, Story>() {
    private val storiesList: MutableList<Story> = mutableListOf()
    @Volatile
    private var lastReceivedIndex: Int = 0

    companion object {
        private var INSTANCE: FilteredDataSource? = null
        fun getInstance(query: String): FilteredDataSource {
            if (INSTANCE == null) {
                synchronized(FilteredDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = FilteredDataSource(query)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Story>) {
        Log.d("filter", "loadInitial() called")
        getMaxStoryId()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { maxStoryId ->
                    lastReceivedIndex = maxStoryId
                    getStories(callback, maxStoryId)
                },
                { error ->
                    Log.d("filter", "Error: $error")
                }
            )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        Log.d("filter", "loadAfter() called")
        getStories(callback, lastReceivedIndex)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        Log.d("filter", "loadBefore() called")
    }

    override fun getKey(item: Story): Long {
        return item.id?.toLong() ?: 0
    }

    private fun getStories(callback: LoadCallback<Story>, maxIndex: Int) {
        if (maxIndex > 0) {
            getListOfStories(maxIndex)
                .subscribeOn(Schedulers.single())
                .subscribe(
                    { stories ->
                        addStoriesToList(stories)
                        callback.onResult(stories)
                        decrementIndexToAvoidRepetition()
                    },
                    { error ->
                        Log.d("filter", "Error in loadInitial(): $error, ${error.stackTrace}")
                    }
                )
        }
    }

    private fun getListOfStories(maxIndex: Int): Single<MutableList<Story>> {
        return Observable.range(0, maxIndex)
            .flatMap({ id ->
                getStoryById((maxIndex - id).toString())
                    .subscribeOn(Schedulers.io())
            }, 25)
            .doOnNext {
                Log.d("filter", "Before - Id: ${it?.id}, Title: ${it?.title}; The current thread is: ${Thread.currentThread()}")
            }
            .filter { story: Story? ->
                story?.title != null && story.type == "story" && story.title.toLowerCase().contains(searchedString.toLowerCase())
            }
            .ofType(Story::class.java)
            .doOnNext {
                Log.d("filter", "After - Id: ${it.id}, Title: ${it.title}; The current thread is: ${Thread.currentThread()}")
                it.id?.apply { if (this < lastReceivedIndex) lastReceivedIndex = this }
            }
            .take(NUM_STORIES_FOR_PAGE.toLong())
            .toList()
    }

    private fun getMaxStoryId(): Single<Int> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getMaxItemId()
    }

    private fun getStoryById(id: String): Observable<Story?> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getStoryById(id)
    }

    private fun addStoriesToList(storiesList: List<Story>) {
        this.storiesList.addAll(storiesList)
    }

    private fun decrementIndexToAvoidRepetition() {
        lastReceivedIndex -= 1
    }
}