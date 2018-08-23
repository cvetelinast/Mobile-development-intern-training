package com.example.tsvetelinastoyanova.hackernewsapp.topnews

import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.Repository
import com.example.tsvetelinastoyanova.hackernewsapp.topnews.TopNewsContract

class TopNewsPresenter(private val view: TopNewsContract.View, private val repository: Repository)
    : TopNewsContract.Presenter {

    override fun start() {
        Log.d("tag", "TopNewsPresenter started")
    }


}