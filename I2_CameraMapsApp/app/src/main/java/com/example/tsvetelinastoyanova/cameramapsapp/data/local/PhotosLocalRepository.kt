package com.example.tsvetelinastoyanova.cameramapsapp.data.local


class PhotosLocalRepository : LocalRepository {

    companion object {
        fun getInstance(): LocalRepository {
            return PhotosLocalRepository()
        }
    }
}