package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class StoriesRemoteDataSource : ItemKeyedDataSource<Long, Story>() {

    private val ids: MutableList<Int> = mutableListOf()
    private var lastReceivedIndex: Int = 13

    companion object {
        private var INSTANCE: StoriesRemoteDataSource? = null
        fun getInstance(): StoriesRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(StoriesRemoteDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoriesRemoteDataSource()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Story>) {
        getTopStoriesIds()
            .flatMapIterable { ids -> this.ids.addAll(ids);ids }
            .take(13)
            .flatMap { id -> getStoryById(id.toString()) }
            .toList()
            .subscribe(
                { stories -> callback.onResult(stories) },
                { error -> Log.d("tag", "Error in loadInitial(): $error") }
            )
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

    private fun getTopStoriesIds(): Observable<List<Int>> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getTopStories()
    }

    private fun getLastStoriesIds(): Observable<List<Int>> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getNewStories()
    }

   /* private fun getTopStories(): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getTopStories()
            .flatMapIterable { ids -> ids }
            .flatMap { id -> getStoryById(id.toString()) }
    }
*/
    private fun getStoryById(id: String): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getStoryById(id)
    }
}
