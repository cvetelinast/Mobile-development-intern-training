package com.example.tsvetelinastoyanova.hackernewsapp.news

import android.arch.paging.PagedList
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class NewsPresenter(private val view: NewsContract.View) : NewsContract.Presenter {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadProperNews(storiesListObservable: Observable<PagedList<Story>>) {
        view.showProgressBar()
        compositeDisposable.add(
            storiesListObservable
                .doOnSubscribe {
                    view.showProgressBar()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { storiesList ->
                        Log.d("ASD", "sub: ${storiesList.size} on ${Thread.currentThread()}")
                        view.submitList(storiesList)
                        view.hideProgressBar()
                    },
                    { err ->
                        Log.d("ASD", "Error loading last news occurred: $err")
                    }
                )
        )
    }

    override fun stopSearching() {
        view.hideProgressBar()
    }

    override fun stopDisposables() {
        view.hideProgressBar()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    override fun start() {}
}
