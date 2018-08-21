package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.content.Context
import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.data.Repository
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import io.reactivex.Observable

class GalleryPresenter(private val view: GalleryContract.View, private val repository: Repository) : GalleryContract.Presenter {
    override fun start() {
        Log.d("tag", "Gallery Presenter started")
    }

    override fun getPhotos(context: Context): Observable<Photo> {
        return repository.getPhotos(context)
    }
}