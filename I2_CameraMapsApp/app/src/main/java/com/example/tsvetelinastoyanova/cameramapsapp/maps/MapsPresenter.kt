package com.example.tsvetelinastoyanova.cameramapsapp.maps

import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.data.Repository


class MapsPresenter(private val view: MapsContract.View, private val repository: Repository) : MapsContract.Presenter {

    override fun start() {
        Log.d("tag", "Maps presenter started")
    }
}