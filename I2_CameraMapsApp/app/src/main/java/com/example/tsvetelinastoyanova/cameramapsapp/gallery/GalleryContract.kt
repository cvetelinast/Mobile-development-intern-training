package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.content.Context
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import com.example.tsvetelinastoyanova.cameramapsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.cameramapsapp.abstraction.BaseView

interface GalleryContract {

    interface View : BaseView<Presenter> {

        fun showSuccessDeletingPhoto()

        fun showErrorDeletingPhoto()

        fun showErrorLoadingPhotos()

        fun showNewPhotoByAddingItToAdapter(photo: Photo)

        fun showAllPhotosOnCompleteLoading()
    }

    interface Presenter : BasePresenter {

        fun getPhotos(context: Context)

        fun deletePhotoFromMemory(photo: Photo)
    }
}