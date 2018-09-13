package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesdatasources

import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.common.Utils.NUM_STORIES_FOR_PAGE
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.GetDataService
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.RetrofitClient
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class AbstractDataSource : ItemKeyedDataSource<Long, Story>(), StoriesRepository {

    abstract fun getStoriesIds(): Observable<List<Int>>
    abstract fun addStoriesToList(storiesList: List<Story>)
    abstract fun getStoriesList(): List<Story>
    abstract fun increaseLastReceivedIndexWithValue(value: Int)
    abstract fun getLastIndex(): Int
    abstract fun getIdsSize(): Int
    abstract fun addIdsFirstTime(listIds: List<Int>)
    abstract fun getSublistOfCachedIds(fromIndex: Int, toIndex: Int): MutableList<Int>

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Story>) {
        if (isFirstTimeLoadingStories()) {
            initGettingIdsAndLoadingStories(callback)
        } else {
            initLoadingCachedStories(callback)
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        Log.d("filter", "loadAfter() called")
        val lastIndex = getLastIndex()
        val storiesNumberToBeLoaded = chooseBestNumberOfStoriesToLoad(lastIndex)
        val sublist =
            getSublistOfCachedIds(lastIndex, lastIndex + storiesNumberToBeLoaded)

        loadStoriesFromCachedIds(sublist, callback)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        Log.d("tag", "loadBefore() called")
    }

    override fun getKey(item: Story): Long {
        return item.id?.toLong() ?: 0
    }

    private fun initLoadingCachedStories(callback: LoadInitialCallback<Story>) {
        Observable.just(getStoriesList())
            .subscribe(
                { stories -> callback.onResult(stories) },
                { error -> Log.d("tag", "Error in loadInitial(): $error") }
            )
    }

    private fun initGettingIdsAndLoadingStories(callback: LoadInitialCallback<Story>) {
        getStoriesIds()
            .doOnNext { idList -> addIdsFirstTime(idList) }
            .flatMapIterable { it }
            .take(NUM_STORIES_FOR_PAGE.toLong())
            .flatMap { id -> getStoryById(id.toString()) }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { stories ->
                    addStoriesToList(stories)
                    increaseLastReceivedIndexWithValue(stories.size)
                    callback.onResult(stories)
                },
                { error -> Log.d("tag", "Error in loadInitial(): $error") }
            )
    }

    open fun loadStoriesFromCachedIds(sublist: MutableList<Int>, callback: LoadCallback<Story>) {
        Observable.fromIterable(sublist)
            .flatMap { id -> getStoryById(id.toString()) }
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .subscribe(
                { stories ->
                    addStoriesToList(stories)
                    callback.onResult(stories)
                },
                { error -> Log.d("tag", "Error in loadAfter(): $error") }
            )
    }

    private fun chooseBestNumberOfStoriesToLoad(lastIndex: Int): Int {
        return if (lastIndex + NUM_STORIES_FOR_PAGE < getIdsSize()) {
            increaseLastReceivedIndexWithValue(NUM_STORIES_FOR_PAGE)
            NUM_STORIES_FOR_PAGE
        } else {
            getIdsSize() - lastIndex
        }
    }

    private fun isFirstTimeLoadingStories() = getLastIndex() == 0

    fun getStoryById(id: String): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getStoryById(id)
            .subscribeOn(Schedulers.io())
            .filter{story: Story? -> story != null}
            .ofType(Story::class.java)
    }
}
