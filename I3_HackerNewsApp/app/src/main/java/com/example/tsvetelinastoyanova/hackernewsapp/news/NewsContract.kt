package com.example.tsvetelinastoyanova.hackernewsapp.news

import android.arch.paging.PagedList
import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BaseView
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

interface NewsContract {

    interface View : BaseView<Presenter> {

        fun showProgressBar()

        fun hideProgressBar()

        fun submitList(list: PagedList<Story>?)
    }

    interface Presenter : BasePresenter {

        fun loadProperNews(storiesListObservable: Observable<PagedList<Story>>)

        fun stopDisposables()
    }
}