package com.example.tsvetelinastoyanova.cameramapsapp.utils

import android.app.Activity
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.Window
import android.view.WindowManager
import com.example.tsvetelinastoyanova.cameramapsapp.data.PhotosRepository
import com.example.tsvetelinastoyanova.cameramapsapp.data.local.LocalRepository
import com.example.tsvetelinastoyanova.cameramapsapp.data.local.PhotosLocalRepository

object Utils {

    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)/*.addToBackStack("TAG")*/
        transaction.commit()
    }

    fun switchFragment(fragmentManager: FragmentManager,
                       newFragment: Fragment, frameId: Int/*, oldFragment: Fragment*/) {
        checkNotNull(fragmentManager)
        checkNotNull(newFragment)
        val transaction = fragmentManager.beginTransaction()
        //   transaction.remove(oldFragment)
        transaction.replace(frameId, newFragment).addToBackStack("TAG")
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
}