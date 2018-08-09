package com.example.tsvetelinastoyanova.cameramapsapp.data.local

import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import android.media.Image
import android.support.annotation.NonNull
import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.R
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PhotosLocalRepository : LocalRepository {
    private var galleryFolder: File? = null

    companion object {
        fun getInstance(): LocalRepository {
            return PhotosLocalRepository()
        }
    }

    override fun getListOfPhotosOneByOne(context: Context): Observable<Photo> {
        createImageGallery(context)
        return Observable.fromIterable(traverseDirectoryAndGetFiles(context))
            .map { file -> convertFileToPhoto(file) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun savePhoto(context: Context, bitmap: Bitmap): Single<String> {
        createImageGallery(context)
        var outputPhoto: FileOutputStream? = null
        try {
            val image = createImageFile()
            var outputPhoto = FileOutputStream(image)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
            return Single.just(image.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputPhoto?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val errorMessage = "Not successfull shot"
        return Single.just(errorMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        /* return Single.fromCallable {
             image.use { image ->
                 FileOutputStream(file).channel.use { output ->
                     output.write(image.planes[0].buffer)
                     return@fromCallable file
                 }
             }
         }*/
    }

    private fun createImageGallery(context: Context) {
        val storageDirectory = context.filesDir.path
        galleryFolder = File(storageDirectory, context.resources.getString(R.string.app_name))
        galleryFolder?.let {
            if (!it.exists()) {
                val wasCreated = it.mkdirs()
                if (!wasCreated) {
                    Log.e("CapturedImages", "Failed to create directory")
                }
            }
        }
    }

    private fun traverseDirectoryAndGetFiles(context: Context): List<File> {
        val list: MutableList<File> = ArrayList()

        val storageDirectory = context.filesDir.absolutePath
        galleryFolder = File(storageDirectory, context.resources.getString(R.string.app_name))
        val folder: File? = galleryFolder
        folder?.let {
            list.addAll(it.listFiles())
        }
        return list

        /* galleryFolder?.let {
             list.a

             for (image in it.listFiles()) {
                 val exif = ExifInterface(image.path)
                 val lat: String = ExifInterface.TAG_GPS_LATITUDE
                 if (lat != null) {
                     val lat_data: String = exif.getAttribute(lat)
                     Log.d("tag", "Location of the file: lat: $lat, lat_data: $lat_data")
                 }
             }
         }*/
    }

    private fun convertFileToPhoto(file: File): Photo {
        val exif = ExifInterface(file.path)
        val lat: String = ExifInterface.TAG_GPS_LATITUDE
        var lat_data: String = "Fake data"//= exif.getAttribute(lat)
        if (lat_data == null) {
            lat_data = "Some fake data"
        }
        return Photo(file.name, Date(file.lastModified()), lat_data, file)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "image_" + timeStamp + "_"
        return File.createTempFile(imageFileName, ".jpg", galleryFolder)
    }

}