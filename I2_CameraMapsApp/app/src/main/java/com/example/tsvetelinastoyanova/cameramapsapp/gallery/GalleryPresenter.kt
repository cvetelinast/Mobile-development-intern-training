package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.content.Context
import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.data.Repository
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo

class GalleryPresenter(private val view: GalleryContract.View, private val repository: Repository) : GalleryContract.Presenter {

    override fun start() {
        Log.d("tag", "Gallery Presenter started")
    }

    override fun getPhotos(context: Context) {
        repository.getPhotos(context)
            .subscribe(
                { photo -> view.showNewPhotoByAddingItToAdapter(photo) },
                { _ -> view.showErrorLoadingPhotos() },
                { view.showAllPhotosOnCompleteLoading() }
            )
    }

    override fun deletePhotoFromMemory(photo: Photo) {
        repository.deletePhoto(photo)
            .subscribe(
                { result ->
                    if (result == true) {
                        view.showSuccessDeletingPhoto()
                    } else {
                        view.showErrorDeletingPhoto()
                    }
                },
                { _ -> view.showErrorDeletingPhoto() }
            )
    }
}