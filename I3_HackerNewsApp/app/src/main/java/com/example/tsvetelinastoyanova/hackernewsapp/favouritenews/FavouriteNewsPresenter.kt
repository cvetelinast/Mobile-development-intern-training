package com.example.tsvetelinastoyanova.hackernewsapp.favouritenews

import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.Repository

class FavouriteNewsPresenter(private val view: FavouriteNewsContract.View, private val repository: Repository)
    : FavouriteNewsContract.Presenter {
    override fun start() {
        Log.d("tag", "FavouriteNewsPresenter started")
    }

}