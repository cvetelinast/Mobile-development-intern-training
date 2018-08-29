package com.example.tsvetelinastoyanova.hackernewsapp.news

import android.arch.paging.PagedList
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRemoteRepository
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class NewsPresenter(private val view: NewsContract.View,
                    private val repository: StoriesRemoteRepository) : NewsContract.Presenter {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadLastNews(storiesListObservable: Observable<PagedList<Story>>) {
        //   stopDisposables()
        compositeDisposable.add(
            repository.loadLastNews(storiesListObservable)
                .subscribe(
                    { storiesList ->
                        view.hideProgressBar()
                        view.submitList(storiesList)
                    },
                    Throwable::printStackTrace
                )
        )
    }

    override fun loadTopNews(storiesListObservable: Observable<PagedList<Story>>) {
        //   stopDisposables()
        compositeDisposable.add(
            repository.loadTopNews(storiesListObservable)
                .subscribe(
                    { storiesList ->
                        view.hideProgressBar()
                        view.submitList(storiesList)
                    },
                    Throwable::printStackTrace
                )
        )
    }

    override fun stopDisposables() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    override fun start() {

    }
}
