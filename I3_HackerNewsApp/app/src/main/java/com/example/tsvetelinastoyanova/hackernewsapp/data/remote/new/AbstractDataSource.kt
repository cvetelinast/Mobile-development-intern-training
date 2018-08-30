package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.new

import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.GetDataService
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.RetrofitClient
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

abstract class AbstractDataSource : ItemKeyedDataSource<Long, Story>(), StoriesRepository {

    companion object {
        private const val STORIES_NUMBER_PER_PAGE: Int = 13
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Story>) {
        if (isFirstTimeLoadingStories()) {
            getStoriesIds()
                .flatMapIterable { idList ->
                    addIdsFirstTime(idList);idList
                }
                .take(STORIES_NUMBER_PER_PAGE.toLong())
                .flatMap { id -> getStoryById(id.toString()) }
                .toList()
                .subscribe(
                    { stories ->
                        addStoriesToList(stories)
                        increaseLastReceivedIndexWithValue(stories.size)
                        callback.onResult(stories)
                    },
                    { error -> Log.d("tag", "Error in loadInitial(): $error") }
                )
        } else {
            Observable.just(getStoriesList())
                .subscribe(
                    { stories -> callback.onResult(stories) },
                    { error -> Log.d("tag", "Error in loadInitial(): $error") }
                )
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        //   synchronized(this) {
        val storiesCountToBeLoaded: Int
        val lastIndex = getLastIndex()

        if (lastIndex + STORIES_NUMBER_PER_PAGE < getIdsSize()) {
            increaseLastReceivedIndexWithValue(STORIES_NUMBER_PER_PAGE)
            storiesCountToBeLoaded = STORIES_NUMBER_PER_PAGE
        } else {
            storiesCountToBeLoaded = getIdsSize() - lastIndex
        }
        val sublist = getSublistOfCachedStories(lastIndex, lastIndex + storiesCountToBeLoaded)

        Observable.fromIterable(sublist)
            .flatMap { id -> getStoryById(id.toString()) }
            .toList()
            .subscribe(
                { stories ->
                    addStoriesToList(stories)
                    callback.onResult(stories)
                },
                { error -> Log.d("tag", "Error in loadAfter(): $error") }
            )
        //    }
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
        return service.getStoryById(id)
    }

    abstract fun getStoriesIds(): Observable<List<Int>>
    abstract fun addStoriesToList(storiesList: List<Story>)
    abstract fun getStoriesList(): List<Story>
    abstract fun increaseLastReceivedIndexWithValue(value: Int)
    abstract fun getLastIndex(): Int
    abstract fun getIdsSize(): Int
    abstract fun addIdsFirstTime(listIds: List<Int>)
    abstract fun getSublistOfCachedStories(fromIndex: Int, toIndex: Int): MutableList<Int>
}
