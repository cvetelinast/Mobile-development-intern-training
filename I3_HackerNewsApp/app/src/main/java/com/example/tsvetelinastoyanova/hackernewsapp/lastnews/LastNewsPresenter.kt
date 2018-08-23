package com.example.tsvetelinastoyanova.hackernewsapp.lastnews

import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.Repository

class LastNewsPresenter(private val view: LastNewsContract.View, private val repository: Repository)
    : LastNewsContract.Presenter {
    override fun start() {
        Log.d("tag", "LastNewsPresenter started")
    }
}