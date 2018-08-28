package com.example.tsvetelinastoyanova.hackernewsapp.topnews

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.support.v4.app.Fragment
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.BaseSchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.NetworkState
import com.example.tsvetelinastoyanova.hackernewsapp.data.Repository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesRemoteDataSourceFactory
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TopNewsPresenter(private val view: TopNewsContract.View, private val repository: Repository, baseSchedulerProvider: BaseSchedulerProvider)
    : TopNewsContract.Presenter {

    //    var storiesList: LiveData<PagedList<Story>>
    var storiesList: Observable<PagedList<Story>>

    private val pageSize = 15

    private val sourceFactory: StoriesRemoteDataSourceFactory = StoriesRemoteDataSourceFactory(baseSchedulerProvider)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        //     storiesList = LivePagedListBuilder<Long, Story>(sourceFactory, config).build()

        storiesList = RxPagedListBuilder<Long, Story>(sourceFactory, config)
            .setFetchScheduler(baseSchedulerProvider.io())
            .setNotifyScheduler(baseSchedulerProvider.ui())
            .buildObservable()
        //   .doOnNext { Log.i("ASD", "TITLE - ${it[0]?.title}") }

        storiesList.subscribe(
            { storiesList ->
                view.submitList(storiesList)
                Log.i("ASD", "TITLE - ${storiesList.joinToString()}")
            },
            Throwable::printStackTrace
        )
    }

//    fun refresh() {
//        sourceFactory.storiesDataSourceLiveData.value!!.invalidate()
//    }
//
//    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<StoriesRemoteDataSource, NetworkState>(
//        sourceFactory.storiesDataSourceLiveData, { it.networkState })
//
//    fun getRefreshState(): LiveData<NetworkState> = Transformations.switchMap<StoriesRemoteDataSource, NetworkState>(
//        sourceFactory.storiesDataSourceLiveData, { it.initialLoad })

    override fun start() {
/*repository.getTopStories()
  .subscribe(
      { story ->
          Log.d("tag", "Success: ${story.title}")
          view.showStoryInRecyclerView(story)
      },
      { error -> Log.d("tag", "Error: $error") }
  )*/

/* repository.getStoryById("17826731")
   .subscribe(
       { result -> Log.d("tag", "Result: $result") },
       { error -> Log.d("tag", "Error: $error") }
   )*/
    }
}
