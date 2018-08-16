package com.example.tsvetelinastoyanova.cameramapsapp.utils

import android.app.Activity
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.Window
import android.view.WindowManager
import com.example.tsvetelinastoyanova.cameramapsapp.data.PhotosRepository
import com.example.tsvetelinastoyanova.cameramapsapp.data.local.LocalRepository
import com.example.tsvetelinastoyanova.cameramapsapp.data.local.PhotosLocalRepository

object Utils {

    const val LOCATION_REQUEST_CODE = 1889
    const val CAMERA_AND_LOCATION_REQUEST_CODE = 1888
    const val RESULT_GALLERY = 0
    const val CAMERA_BACKGROUND_THREAD_NAME = "CAMERA_BACKGROUND_THREAD"
    const val GALLERY_FRAGMENT_NAME = "GALLERY"
    const val CAMERA_FRAGMENT_NAME = "CAMERA"
    const val MAPS_FRAGMENT_NAME = "MAPS"

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
}