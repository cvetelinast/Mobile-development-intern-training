package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.content.Context
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView
import io.reactivex.Observable

interface GalleryContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

        fun getListOfPhotosOneByOne(context: Context): Observable<Photo>
    }
}