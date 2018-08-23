package com.example.tsvetelinastoyanova.hackernewsapp.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.tsvetelinastoyanova.hackernewsapp.data.NewsRepository

object Utils {

    fun switchFragment(fragmentManager: FragmentManager,
                       newFragment: Fragment, frameId: Int, nameOfFragment: String) {
        checkNotNull(fragmentManager)
        checkNotNull(newFragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, newFragment, nameOfFragment)
        transaction.commit()
    }

    fun provideRepository(): NewsRepository {
        // todo
       // val localRepository =
        return NewsRepository()
    }
}

