package com.example.tsvetelinastoyanova.hackernewsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val fragmentsLoader: FragmentsLoader = FragmentsLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBottomNavigationListener()
        if (supportFragmentManager.fragments.size == 0) {
            loadTopNewsFragment()
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
        fragmentsLoader.loadTopNewsFragment(supportFragmentManager)
    }

    private fun loadLastNewsFragment() {
        fragmentsLoader.loadLastNewsFragment(supportFragmentManager)
    }

    private fun loadFavouriteNewsFragment() {
        fragmentsLoader.loadFavouriteNewsFragment(supportFragmentManager)
    }
}
