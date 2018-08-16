package com.example.tsvetelinastoyanova.cameramapsapp.maps

import android.content.Context
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import com.example.tsvetelinastoyanova.cameramapsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.cameramapsapp.abstraction.BaseView

interface MapsContract {

    interface View : BaseView<Presenter> {
        fun addMarker(photo: Photo)
    }

    interface Presenter : BasePresenter {

        fun getPhotos(context: Context)
    }
}