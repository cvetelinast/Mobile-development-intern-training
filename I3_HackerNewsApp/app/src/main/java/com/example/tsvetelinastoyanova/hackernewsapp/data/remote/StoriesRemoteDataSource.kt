package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.BaseSchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.NetworkState
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Completable
import io.reactivex.Observable

class StoriesRemoteDataSource(
    private val baseSchedulerProvider: BaseSchedulerProvider)
    : ItemKeyedDataSource<Long, Story>() {

    private val ids: MutableList<Int> = mutableListOf()

/*    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()*/

    /**
     * Keep Completable reference for the retry event
     */
    private var retryCompletable: Completable? = null

    private var lastReceivedIndex: Int = 10

    companion object {

        private var INSTANCE: StoriesRemoteDataSource? = null

        fun getInstance(baseSchedulerProvider: BaseSchedulerProvider): StoriesRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(StoriesRemoteDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoriesRemoteDataSource(baseSchedulerProvider)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Story>) {
        /*    networkState.postValue(NetworkState.LOADING)
            initialLoad.postValue(NetworkState.LOADING)
    */
        getTopStoriesIds()
            .flatMapIterable { ids -> this.ids.addAll(ids);ids }
            //    .doOnNext { Log.i("THR", "observable after flatMap on ${Thread.currentThread()}" ) }
            .take(10)
            .flatMap { id ->
                getStoryById(id.toString())/*.map {
                    //   Log.i("tag1", "story title: ${it.title}")
                    it
                }*/
            }
            .toList()
            .subscribe(
                { stories ->
                    callback.onResult(stories)
                },
                { throwable ->
                    Log.d("tag", "Error in loadInitial(): $throwable")
                    //     val error = NetworkState.error(throwable.message)
                    /*        // publish the error
                            networkState.postValue(error)
                            initialLoad.postValue(error)*/
                }
            )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        // set network value to loading.
        //     networkState.postValue(NetworkState.LOADING)
        var storiesCountToBeLoaded: Int
        Log.d("tag1", "loadAfter() called")
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
                { stories ->
                    callback.onResult(stories)
                    Log.d("tag1", "Success in loadAfter(): Loaded stories: ${stories.size}")
                },
                { error -> Log.d("tag1", "Error in loadAfter(): $error") }
            )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Story>) {
        Log.d("tag1", "loadBefore() called")
    }

    override fun getKey(item: Story): Long {
        return item.id.toLong()
    }

    fun getTopStoriesIds(): Observable<List<Int>> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getTopStories()
//            .subscribeOn(baseSchedulerProvider.io())
//            .observeOn(baseSchedulerProvider.ui())
    }

    /*override*/ fun getTopStories(): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getTopStories()
            .flatMapIterable { ids -> ids }
            .flatMap { id -> getStoryById(id.toString()) }
        //     .subscribeOn(baseSchedulerProvider.io())
        //     .observeOn(baseSchedulerProvider.ui())
    }

    /*override*/ fun getStoryById(id: String): Observable<Story> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getStoryById(id)
//            .subscribeOn(baseSchedulerProvider.io())
//            .observeOn(baseSchedulerProvider.ui())
    }
}
