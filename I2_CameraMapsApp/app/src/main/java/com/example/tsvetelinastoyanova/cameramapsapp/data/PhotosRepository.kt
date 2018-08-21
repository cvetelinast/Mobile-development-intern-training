package com.example.tsvetelinastoyanova.cameramapsapp.data

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import com.example.tsvetelinastoyanova.cameramapsapp.data.local.LocalRepository
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

class PhotosRepository(private val localRepository: LocalRepository) : Repository {

    companion object {

        @Volatile
        private var INSTANCE: PhotosRepository? = null
        fun getInstance(localRepository: LocalRepository): PhotosRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PhotosRepository(localRepository).also { INSTANCE = it }
            }
        }

    }
    override fun getPhotos(context: Context): Observable<Photo> {
        return localRepository.getPhotos(context)
    }

    override fun savePhoto(context: Context, bitmap: Bitmap, location: Location?): Single<File> {
       return localRepository.savePhoto(context, bitmap, location)
    }
}