package com.example.tsvetelinastoyanova.hackernewsapp.topnews

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.BaseSchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.Repository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesRemoteDataSourceFactory
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class TopNewsPresenter(private val view: TopNewsContract.View, private val repository: Repository, baseSchedulerProvider: BaseSchedulerProvider)
    : TopNewsContract.Presenter {

    var storiesList: Observable<PagedList<Story>>

    private val pageSize = 15

    private val sourceFactory: StoriesRemoteDataSourceFactory = StoriesRemoteDataSourceFactory()

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()

        storiesList = RxPagedListBuilder<Long, Story>(sourceFactory, config)
            .setFetchScheduler(baseSchedulerProvider.io())
            .setNotifyScheduler(baseSchedulerProvider.ui())
            .buildObservable()
    }

    override fun start() {
        view.showProgressBar()
        storiesList
            .subscribe(
                { storiesList ->
                    view.hideProgressBar()
                    view.submitList(storiesList)
                },
                Throwable::printStackTrace
            )
    }
}
