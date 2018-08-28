package com.example.tsvetelinastoyanova.hackernewsapp

import android.support.v4.app.FragmentManager
import com.example.tsvetelinastoyanova.hackernewsapp.common.Constants
import com.example.tsvetelinastoyanova.hackernewsapp.common.Utils
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.SchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository
import com.example.tsvetelinastoyanova.hackernewsapp.favouritenews.FavouriteNewsContract
import com.example.tsvetelinastoyanova.hackernewsapp.favouritenews.FavouriteNewsFragment
import com.example.tsvetelinastoyanova.hackernewsapp.favouritenews.FavouriteNewsPresenter
import com.example.tsvetelinastoyanova.hackernewsapp.lastnews.LastNewsContract
import com.example.tsvetelinastoyanova.hackernewsapp.lastnews.LastNewsFragment
import com.example.tsvetelinastoyanova.hackernewsapp.lastnews.LastNewsPresenter
import com.example.tsvetelinastoyanova.hackernewsapp.topnews.TopNewsContract
import com.example.tsvetelinastoyanova.hackernewsapp.topnews.TopNewsFragment
import com.example.tsvetelinastoyanova.hackernewsapp.topnews.TopNewsPresenter
import io.reactivex.disposables.CompositeDisposable

class FragmentsLoader {
    fun loadTopNewsFragment(fragmentManager: FragmentManager) {
        val topNewsFragment = TopNewsFragment.newInstance()
        Utils.switchFragment(fragmentManager, topNewsFragment, R.id.contentFragment, Constants.TOP_NEWS_FRAGMENT_NAME)
        val compositeDisposable = CompositeDisposable()
        val repository: StoriesRepository = Utils.provideRepository(SchedulerProvider.getInstance())
        val topNewsPresenter: TopNewsContract.Presenter = TopNewsPresenter(topNewsFragment, repository, SchedulerProvider.getInstance())
        topNewsFragment.setPresenter(topNewsPresenter)
    }

    fun loadLastNewsFragment(fragmentManager: FragmentManager) {
        val lastNewsFragment = LastNewsFragment.newInstance()
        Utils.switchFragment(fragmentManager, lastNewsFragment, R.id.contentFragment, Constants.LAST_NEWS_FRAGMENT_NAME)
        val compositeDisposable = CompositeDisposable()
        val repository: StoriesRepository = Utils.provideRepository(SchedulerProvider.getInstance())
        val lastNewsPresenter: LastNewsContract.Presenter = LastNewsPresenter(lastNewsFragment, repository)
        lastNewsFragment.setPresenter(lastNewsPresenter)
    }

    fun loadFavouriteNewsFragment(fragmentManager: FragmentManager) {
        val favouriteNewsFragment = FavouriteNewsFragment.newInstance()
        Utils.switchFragment(fragmentManager, favouriteNewsFragment, R.id.contentFragment, Constants.FAVOURITE_NEWS_FRAGMENT_NAME)
        val compositeDisposable = CompositeDisposable()
        val repository: StoriesRepository = Utils.provideRepository(SchedulerProvider.getInstance())
        val favouriteNewsPresenter: FavouriteNewsContract.Presenter = FavouriteNewsPresenter(favouriteNewsFragment, repository)
        favouriteNewsFragment.setPresenter(favouriteNewsPresenter)
    }
}