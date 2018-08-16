package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.content.Context
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import com.example.tsvetelinastoyanova.cameramapsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.cameramapsapp.abstraction.BaseView
import io.reactivex.Observable

interface GalleryContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

        fun getPhotos(context: Context): Observable<Photo>
    }
}