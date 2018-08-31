package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesDataSources

import android.arch.paging.ItemKeyedDataSource
import android.util.AndroidException
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.GetDataService
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.RetrofitClient
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class AbstractDataSource : ItemKeyedDataSource<Long, Story>(), StoriesRepository {

    companion object {
        private const val STORIES_NUMBER_PER_PAGE: Int = 13
    }

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
            .take(STORIES_NUMBER_PER_PAGE.toLong())
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

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        val storiesNumberToBeLoaded: Int
        val lastIndex = getLastIndex()

        storiesNumberToBeLoaded = chooseBestNumberOfStoriesToLoad(lastIndex)
        val sublist = getSublistOfCachedIds(lastIndex, lastIndex + storiesNumberToBeLoaded)

        loadStoriesFromCachedIds(sublist, callback)
    }

    private fun loadStoriesFromCachedIds(sublist: MutableList<Int>, callback: LoadCallback<Story>) {
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
        return if (lastIndex + STORIES_NUMBER_PER_PAGE < getIdsSize()) {
            increaseLastReceivedIndexWithValue(STORIES_NUMBER_PER_PAGE)
            STORIES_NUMBER_PER_PAGE
        } else {
            getIdsSize() - lastIndex
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        Log.d("tag", "loadBefore() called")
    }

    override fun getKey(item: Story): Long {
        return item.id.toLong()
    }

    private fun isFirstTimeLoadingStories() = getLastIndex() == 0

    private fun getStoryById(id: String): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getStoryById(id).subscribeOn(Schedulers.io())
    }
}
