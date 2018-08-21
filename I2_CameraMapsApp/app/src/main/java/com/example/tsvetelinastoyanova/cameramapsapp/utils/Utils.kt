package com.example.tsvetelinastoyanova.cameramapsapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.FileProvider
import android.view.Window
import android.view.WindowManager
import com.example.tsvetelinastoyanova.cameramapsapp.BuildConfig
import com.example.tsvetelinastoyanova.cameramapsapp.data.PhotosRepository
import com.example.tsvetelinastoyanova.cameramapsapp.data.local.LocalRepository
import com.example.tsvetelinastoyanova.cameramapsapp.data.local.PhotosLocalRepository
import java.io.File

object Utils {

    private const val PACKAGE_NAME_PROVIDER = ".my.package.name.provider"
    private const val TYPE_IMAGE = "image/*"
    const val LOCATION_REQUEST_CODE = 1889
    const val CAMERA_AND_LOCATION_REQUEST_CODE = 1888
    const val CAMERA_BACKGROUND_THREAD_NAME = "CAMERA_BACKGROUND_THREAD"
    const val GALLERY_FRAGMENT_NAME = "GALLERY"
    const val CAMERA_FRAGMENT_NAME = "CAMERA"

    const val MAPS_FRAGMENT_NAME = "MAPS"
    const val OPEN_GALLERY_FRAGMENT = "OPEN_GALLERY_FRAGMENT"
    const val OPEN_MAPS_FRAGMENT = "OPEN_MAPS_FRAGMENT"
    const val OPEN_CAMERA_FRAGMENT = "OPEN_CAMERA_FRAGMENT"
    const val PATHS = "PATHS"
    const val END = "END"

    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int, nameOfFragment: String) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment, nameOfFragment)
        transaction.commit()
    }

    fun switchFragment(fragmentManager: FragmentManager,
                       newFragment: Fragment, frameId: Int, nameOfFragment: String) {
        checkNotNull(fragmentManager)
        checkNotNull(newFragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, newFragment, nameOfFragment)
        transaction.commit()
    }

    fun providePhotosRepository(): PhotosRepository {
        val localRepository: LocalRepository = PhotosLocalRepository.getInstance()
        return PhotosRepository.getInstance(localRepository)
    }

    fun setTranslucent(activity: Activity, translucent: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val w: Window = activity.window
            if (translucent) {
                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            } else {
                w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    fun requestPermissions(fragment: Fragment, array: Array<String>, code: Int) {
        fragment.requestPermissions(array, code)
    }

    fun onClickLoadPhotoInGallery(file: File, activity: Activity) {
        val uri: Uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + PACKAGE_NAME_PROVIDER, file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, TYPE_IMAGE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivity(intent)
    }
}