package com.example.tsvetelinastoyanova.cameramapsapp.data

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
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
    override fun getListOfPhotosOneByOne(context: Context): Observable<Photo> {
        return localRepository.getListOfPhotosOneByOne(context)
    }

    override fun savePhoto(context: Context, bitmap: Bitmap): Single<String> {
       return localRepository.savePhoto(context, bitmap)
    }

}