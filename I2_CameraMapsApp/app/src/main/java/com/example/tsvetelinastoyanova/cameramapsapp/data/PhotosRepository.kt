package com.example.tsvetelinastoyanova.cameramapsapp.data

import com.example.tsvetelinastoyanova.cameramapsapp.data.local.LocalRepository

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
}