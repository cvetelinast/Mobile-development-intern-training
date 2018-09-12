package com.example.tsvetelinastoyanova.hackernewsapp

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.webkit.WebView
import com.example.tsvetelinastoyanova.hackernewsapp.common.Utils
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.SchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRemoteRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.TypeRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesdatasources.NewStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesdatasources.TopStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.news.NewsContract
import com.example.tsvetelinastoyanova.hackernewsapp.news.NewsFragment
import com.example.tsvetelinastoyanova.hackernewsapp.news.NewsPresenter
import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    private var searchView: SearchView? = null
    private var presenter: NewsContract.Presenter? = null

    companion object {
        const val ACTION = "ACTION"
        const val TOP_NEWS = "TOP_NEWS"
        const val LAST_NEWS = "LAST_NEWS"
        const val FAVOURITE_NEWS = "FAVOURITE_NEWS"
        const val SEARCH = "SEARCH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()

        val fragment = getFragment()
        presenter = setAndReturnPresenter(fragment)

        if (savedInstanceState != null) with(savedInstanceState) {
            intent.action = getString(ACTION) ?: ""
        }
        val tempPresenter = presenter
        if (tempPresenter != null && intent.action != null) {
            loadNews(intent.action, tempPresenter)
            setBottomNavigationListener(tempPresenter)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putString(ACTION, intent.action)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        setSearchViewDetails(searchView, searchManager)
        this.searchView = searchView
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == R.id.action_search) true else super.onOptionsItemSelected(item)

    override fun onBackPressed() {
        val webView = findViewById<WebView>(R.id.webView)
        when {
            webView.visibility == View.VISIBLE -> webView.visibility = View.INVISIBLE
            searchView?.isIconified != true -> searchView?.isIconified = true
            else -> super.onBackPressed()
        }
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.app_name)
        }
        setSupportActionBar(toolbar)
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

    private fun getFragment(): NewsFragment {
        var topNewsFragment = supportFragmentManager.findFragmentById(R.id.contentFragment) as? NewsFragment
        if (topNewsFragment == null) {
            topNewsFragment = getNewFragment()
        }
        return topNewsFragment
    }

    private fun getNewFragment(): NewsFragment {
        val topNewsFragment = NewsFragment.newInstance()
        Utils.addFragment(supportFragmentManager, topNewsFragment, R.id.contentFragment, Utils.TOP_NEWS_FRAGMENT_NAME)
        return topNewsFragment
    }

    private fun setAndReturnPresenter(topNewsFragment: NewsFragment): NewsContract.Presenter {
        val repository = StoriesRemoteRepository
            .getInstance(TopStoriesRemoteDataSource.getInstance(), NewStoriesRemoteDataSource.getInstance())
        val topNewsPresenter: NewsContract.Presenter = NewsPresenter(topNewsFragment, repository)
        topNewsFragment.setPresenter(topNewsPresenter)
        return topNewsPresenter
    }

    private fun loadNews(action: String, presenter: NewsContract.Presenter) {
        when {
            action.contains(FAVOURITE_NEWS) -> loadFavouriteNews(presenter)
            action.contains(LAST_NEWS) -> loadLastNews(presenter)
            action.contains(SEARCH) -> Log.d("OOO", "search")
            else -> loadTopNews(presenter)
        }
    }

    private fun loadTopNews(presenter: NewsContract.Presenter) {
        intent.action = TOP_NEWS
        hideWebView()
        val observable = Utils.provideStoriesObservable(
            TypeRemoteDataSource.TOP_STORIES, SchedulerProvider.getInstance(), "")
        presenter.loadProperNews(observable)
    }

    private fun loadLastNews(presenter: NewsContract.Presenter) {
        intent.action = LAST_NEWS
        hideWebView()
        val observable = Utils.provideStoriesObservable(
            TypeRemoteDataSource.NEW_STORIES, SchedulerProvider.getInstance(), "")
        presenter.loadProperNews(observable)
    }

    private fun loadFavouriteNews(presenter: NewsContract.Presenter) {
        intent.action = FAVOURITE_NEWS
        hideWebView()
    }

    private fun setSearchViewDetails(searchView: SearchView, searchManager: SearchManager) {
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        setSearchListener(searchView)
    }

    private fun setSearchListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                intent.action = SEARCH
                searchView.setQuery(query, false)
                loadFilteredNews(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
    }

    private fun loadFilteredNews(searchedString: String) {
        val storiesObservable = Utils.provideStoriesObservable(
            TypeRemoteDataSource.FILTERED_STORIES, SchedulerProvider.getInstance(), searchedString)
        presenter!!.loadProperNews(storiesObservable)
    }

    private fun hideWebView() {
        val webView: WebView? = findViewById(R.id.webView)
        webView?.let {
            if (it.visibility == View.VISIBLE) {
                it.visibility = View.INVISIBLE
            }
        }
    }
}
