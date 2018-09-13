package com.example.tsvetelinastoyanova.hackernewsapp.news

import android.arch.paging.PagedList
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRemoteRepository
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class NewsPresenter(private val view: NewsContract.View,
                    private val repository: StoriesRemoteRepository) : NewsContract.Presenter {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadProperNews(storiesListObservable: Observable<PagedList<Story>>) {
        view.showProgressBar()
    //    compositeDisposable.add(
            storiesListObservable
               /* .doOnSubscribe {
                    view.showProgressBar()
                }*/
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Log.i("thread", "on ${Thread.currentThread()}")
                    view.showProgressBar()
                }
                // .doAfterNext { view.hideProgressBar() }
                .subscribe(
                    { storiesList ->
                        Log.d("thread", "${storiesList.size}")
                        view.submitList(storiesList)
                        view.hideProgressBar()
                    },
                    { err ->
                        Log.d("thread", "Error loading last news occurred: $err")
                    }
                )
  //      )
    }

    override fun stopSearching() {
        view.hideProgressBar()
    }

    override fun stopDisposables() {
        view.hideProgressBar()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    override fun start() {

    }
}
