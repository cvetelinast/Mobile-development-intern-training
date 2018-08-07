package com.example.tsvetelinastoyanova.cameramapsapp.data

import android.content.Context
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import io.reactivex.Observable

interface Repository {

    fun getListOfPhotosOneByOne(context: Context): Observable<Photo>
}