package com.example.tsvetelinastoyanova.cameramapsapp.data.local

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

interface LocalRepository {

    fun getPhotos(context: Context): Observable<Photo>

    fun savePhoto(context: Context, bitmap: Bitmap, location: Location?): Single<File>
}