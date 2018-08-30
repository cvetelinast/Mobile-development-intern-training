package com.example.tsvetelinastoyanova.hackernewsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.example.tsvetelinastoyanova.hackernewsapp.common.Constants
import com.example.tsvetelinastoyanova.hackernewsapp.common.Utils
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.SchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRemoteRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesDataSourceFactory
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.TypeRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.new.NewStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.top.TopStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.news.NewsContract
import com.example.tsvetelinastoyanova.hackernewsapp.news.NewsFragment
import com.example.tsvetelinastoyanova.hackernewsapp.news.NewsPresenter

class MainActivity : AppCompatActivity() {

    private var action: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = loadFragment()
        val presenter = setAndReturnPresenter(fragment)

        if (savedInstanceState != null) with(savedInstanceState) {
            action = getString(ACTION) ?: ""
        }
        loadNews(action, presenter)
        setBottomNavigationListener(presenter)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putString(ACTION, action)
        }
        super.onSaveInstanceState(outState)
    }

    private fun setBottomNavigationListener(presenter: NewsContract.Presenter) {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_top -> loadTopNews(presenter)
                R.id.action_new -> loadLastNews(presenter)
                R.id.action_favourite -> loadFavouriteNews(presenter)
            }
            true
        }
    }

    private fun loadFragment(): NewsFragment {
        var topNewsFragment = supportFragmentManager.findFragmentById(R.id.contentFragment) as? NewsFragment
        if (topNewsFragment == null) {
            topNewsFragment = NewsFragment.newInstance()
            Utils.addFragment(supportFragmentManager, topNewsFragment, R.id.contentFragment, Constants.TOP_NEWS_FRAGMENT_NAME)
        }
        return topNewsFragment
    }

    private fun setAndReturnPresenter(topNewsFragment: NewsFragment): NewsContract.Presenter {
        // val repository = StoriesRemoteRepository(TopStoriesRemoteDataSource.getInstance(), NewStoriesRemoteDataSource.getInstance())
        val repository = StoriesRemoteRepository.getInstance(TopStoriesRemoteDataSource.getInstance(), NewStoriesRemoteDataSource.getInstance())
        val topNewsPresenter: NewsContract.Presenter = NewsPresenter(topNewsFragment, repository)
        topNewsFragment.setPresenter(topNewsPresenter)
        return topNewsPresenter
    }

    private fun loadNews(action: String, presenter: NewsContract.Presenter) {
        when (action) {
            FAVOURITE_NEWS -> loadFavouriteNews(presenter)
            LAST_NEWS -> loadLastNews(presenter)
            else -> loadTopNews(presenter)

        }
    }

    private fun loadTopNews(presenter: NewsContract.Presenter) {
        action = TOP_NEWS
        val observable = Utils.provideStoriesObservable(TypeRemoteDataSource.TOP_STORIES, SchedulerProvider.getInstance())
        presenter.loadTopNews(observable)
    }

    private fun loadLastNews(presenter: NewsContract.Presenter) {
        action = LAST_NEWS
        val observable = Utils.provideStoriesObservable(TypeRemoteDataSource.NEW_STORIES, SchedulerProvider.getInstance())
        presenter.loadLastNews(observable)
    }

    private fun loadFavouriteNews(presenter: NewsContract.Presenter) {
        action = FAVOURITE_NEWS
        // presenter.loadFavouriteNews()
    }

    companion object {
        const val ACTION = "ACTION"
        const val TOP_NEWS = "TOP_NEWS"
        const val LAST_NEWS = "LAST_NEWS"
        const val FAVOURITE_NEWS = "FAVOURITE_NEWS"
    }
}
