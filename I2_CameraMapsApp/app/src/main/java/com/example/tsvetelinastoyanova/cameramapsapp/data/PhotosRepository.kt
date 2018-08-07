package com.example.tsvetelinastoyanova.cameramapsapp.data

import android.content.Context
import com.example.tsvetelinastoyanova.cameramapsapp.data.local.LocalRepository
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import io.reactivex.Observable

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
}