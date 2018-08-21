package com.example.tsvetelinastoyanova.cameramapsapp.data.local

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.media.ExifInterface
import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.R
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import com.google.android.gms.maps.model.LatLng
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

    override fun savePhoto(context: Context, bitmap: Bitmap, location: Location?): Single<File> {
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
                if (location != null) {
                    setLocationTagsToCreatedFile(s, location)
                }
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
        val lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE) ?: ""
        val northOrSouth = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) ?: ""
        val lon = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) ?: ""
        val eastOrWest = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) ?: ""

        Log.d("location", "lat: $lat, lon: $lon")
        var location: LatLng? = null
        if (lat != "" && lon != "") {
            location = LatLng(convertRationalToLocation(lat, northOrSouth), convertRationalToLocation(lon, eastOrWest))
        }
        return Photo(file.name, Date(file.lastModified()), location, file)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "image_" + timeStamp + "_"
        return File.createTempFile(imageFileName, ".jpg", galleryFolder)
    }

    private fun setLocationTagsToCreatedFile(s: File, location: Location) {
        val exif = ExifInterface(s.path)

        val lat = location.latitude
        val latitudeStr = convertLocationToRational(lat)
        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr)
        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, if (lat > 0) "N" else "S")

        val lon = location.longitude
        val longitudeStr = convertLocationToRational(lon)
        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitudeStr)
        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, if (lon > 0) "E" else "W")

        exif.saveAttributes()
    }

    private fun convertLocationToRational(coordinate: Double): String {
        val absoluteValue = Math.abs(coordinate)
        val valueInSeconds = Location.convert(absoluteValue, Location.FORMAT_SECONDS)
        val splits = valueInSeconds.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val secondPartSplit = splits[2].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val seconds: String = if (secondPartSplit.isEmpty()) {
            splits[2]
        } else {
            secondPartSplit[0]
        }
        return splits[0] + "/1," + splits[1] + "/1," + seconds + "/1"
    }

    private fun convertRationalToLocation(rationalString: String, direction: String): Double {
        val sign: Int = if (direction == "N" || direction == "E") 1 else -1
        if (rationalString == "") {
            return 0.0
        }
        val split = rationalString.split(",".toRegex())
            .map { s -> s.split("/".toRegex())[0] }
            .joinToString(":")
        return sign * Location.convert(split)
    }
}