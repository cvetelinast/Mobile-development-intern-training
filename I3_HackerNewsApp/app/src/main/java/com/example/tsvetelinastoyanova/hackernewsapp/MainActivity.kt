package com.example.tsvetelinastoyanova.hackernewsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val fragmentsLoader: FragmentsLoader = FragmentsLoader()
    private var currentFragmentAction: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBottomNavigationListener()

        if (savedInstanceState != null) with(savedInstanceState) {
            currentFragmentAction = getString(CURRENT_FRAGMENT_ACTION) ?: ""
        }

        loadParticularFragment()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putString(CURRENT_FRAGMENT_ACTION, currentFragmentAction)
        }
        super.onSaveInstanceState(outState)
    }

    private fun loadParticularFragment() {
        when (currentFragmentAction) {
            ACTION_GET_LAST_NEWS -> {
                loadLastNewsFragment()
            }
            ACTION_GET_FAVOURITE_NEWS -> {
                loadFavouriteNewsFragment()
            }
            else -> {
                loadTopNewsFragment()
            }
        }
    }

    private fun setBottomNavigationListener() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_top -> loadTopNewsFragment()
                R.id.action_new -> loadLastNewsFragment()
                R.id.action_favourite -> loadFavouriteNewsFragment()
            }
            true
        }
    }

    private fun loadTopNewsFragment() {
        currentFragmentAction = ACTION_GET_TOP_NEWS
        fragmentsLoader.loadTopNewsFragment(supportFragmentManager)
    }

    private fun loadLastNewsFragment() {
        currentFragmentAction = ACTION_GET_LAST_NEWS
        fragmentsLoader.loadLastNewsFragment(supportFragmentManager)
    }

    private fun loadFavouriteNewsFragment() {
        currentFragmentAction = ACTION_GET_FAVOURITE_NEWS
        fragmentsLoader.loadFavouriteNewsFragment(supportFragmentManager)
    }

    companion object {
        const val CURRENT_FRAGMENT_ACTION = "CURRENT_FRAGMENT_ACTION"
        const val ACTION_GET_TOP_NEWS = "GET_TOP_NEWS"
        const val ACTION_GET_LAST_NEWS = "GET_LAST_NEWS"
        const val ACTION_GET_FAVOURITE_NEWS = "GET_FAVOURITE_NEWS"
    }
}
