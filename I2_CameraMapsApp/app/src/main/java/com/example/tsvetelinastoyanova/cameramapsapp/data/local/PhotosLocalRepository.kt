package com.example.tsvetelinastoyanova.cameramapsapp.data.local

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.media.ExifInterface
import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.R
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PhotosLocalRepository : LocalRepository {
    private var galleryFolder: File? = null

    companion object {
        fun getInstance(): LocalRepository {
            return PhotosLocalRepository()
        }
    }

    override fun getPhotos(context: Context): Observable<Photo> {
        createImageGallery(context)
        return Observable.fromIterable(traverseDirectoryAndGetFiles(context))
            .map(::convertFileToPhoto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun savePhoto(context: Context, bitmap: Bitmap, location: Location): Single<File> {
        createImageGallery(context)

        return Single.fromCallable {
            val image = createImageFile()
            var outputPhoto: FileOutputStream? = null

            try {
                outputPhoto = FileOutputStream(image)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputPhoto)
            } finally {
                outputPhoto?.close()
            }
            image
        }
            .doAfterSuccess { s ->
                val exifInterface = ExifInterface(s.path)
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, location.latitude.toString())
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, location.longitude.toString())
                exifInterface.saveAttributes()
                Log.d("fsd", "vgero")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
    }

    private fun convertFileToPhoto(file: File): Photo {
        val exifInterface = ExifInterface(file.path)
        val latTemp: String = ExifInterface.TAG_GPS_LATITUDE
        val lat2 = exifInterface.getAttribute(latTemp)

        val lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE) ?: ""
        val lon = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) ?: ""
        Log.d("location in repository", "lat: $lat, lon: $lon, latTemp: $latTemp, lat2: $lat2")

        /*var lat_data: String = "Fake data"//= exif.getAttribute(lat)
        if (lat_data == null) {
            lat_data = "Some fake data"
        }*/
        return Photo(file.name, Date(file.lastModified()), lat, lon, file)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "image_" + timeStamp + "_"
        return File.createTempFile(imageFileName, ".jpg", galleryFolder)
    }

}