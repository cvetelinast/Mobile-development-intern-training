package com.example.tsvetelinastoyanova.cameramapsapp.data.local

import android.content.Context
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import io.reactivex.Observable

interface LocalRepository {

    fun getListOfPhotosOneByOne(context: Context): Observable<Photo>
}