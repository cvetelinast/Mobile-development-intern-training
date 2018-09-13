package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.filtereddatasources

import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.common.Utils.NUM_STORIES_FOR_SEARCH
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

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Story>) {
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
        getStories(callback, lastReceivedIndex)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Story>) {}

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
                        decrementIndexToAvoidRepetition()
                        callback.onResult(stories)
                    },
                    { error ->
                        Log.d("filter", "Error in getStories(): $error, ${error.stackTrace}")
                    }
                )
        }
    }

    private fun getListOfStories(maxIndex: Int): Single<MutableList<Story>> {
        return Observable.range(0, maxIndex)
            .flatMap({ id ->
                getStoryById((maxIndex - id).toString())
                    .subscribeOn(Schedulers.io())
            }, true, 400)
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
            .take(NUM_STORIES_FOR_SEARCH.toLong())
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