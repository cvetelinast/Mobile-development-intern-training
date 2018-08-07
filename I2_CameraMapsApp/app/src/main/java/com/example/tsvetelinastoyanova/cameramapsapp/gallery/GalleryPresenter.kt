package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.data.Repository

class GalleryPresenter(private val view: GalleryContract.View, private val repository: Repository) : GalleryContract.Presenter {
    override fun start() {
        Log.d("tag", "Gallery Presenter started")
    }
}