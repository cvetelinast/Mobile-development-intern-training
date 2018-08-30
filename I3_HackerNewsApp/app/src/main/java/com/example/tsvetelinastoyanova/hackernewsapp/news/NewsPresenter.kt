package com.example.tsvetelinastoyanova.hackernewsapp.news

import android.arch.paging.PagedList
import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRemoteRepository
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class NewsPresenter(private val view: NewsContract.View,
                    private val repository: StoriesRemoteRepository) : NewsContract.Presenter {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadLastNews(storiesListObservable: Observable<PagedList<Story>>) {
        view.showProgressBar()
        compositeDisposable.add(
            //repository.loadLastNews(storiesListObservable)
            storiesListObservable
                .doOnNext {
                    view.showProgressBar()
                }
                .subscribe(
                    { storiesList ->
                        view.hideProgressBar()
                        view.submitList(storiesList)
                    },
                    { err ->
                        Log.d("tag", "Error loading last news occurred: $err")
                    }
                )
        )
    }

    override fun loadTopNews(storiesListObservable: Observable<PagedList<Story>>) {
        view.showProgressBar()
        // repository.loadTopNews(storiesListObservable)
        storiesListObservable
            .doOnNext {
                view.showProgressBar()
            }
            .subscribe(
                { storiesList ->
                    view.hideProgressBar()
                    view.submitList(storiesList)
                },
                { err ->
                    Log.d("tag", "Error loading top news occurred: $err")
                }
            )
    }

    override fun stopDisposables() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    override fun start() {

    }
}
