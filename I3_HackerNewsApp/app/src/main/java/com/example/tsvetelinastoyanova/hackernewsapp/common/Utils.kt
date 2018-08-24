package com.example.tsvetelinastoyanova.hackernewsapp.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.local.StoriesLocalDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesRemoteDataSource

object Utils {

    fun switchFragment(fragmentManager: FragmentManager,
                       newFragment: Fragment, frameId: Int, nameOfFragment: String) {
        checkNotNull(fragmentManager)
        checkNotNull(newFragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, newFragment, nameOfFragment)
        transaction.commit()
    }

    fun provideRepository(): StoriesRepository {
        val localDataSource = StoriesLocalDataSource.getInstance()
        val remoteDataSource = StoriesRemoteDataSource.getInstance()
        return StoriesRepository(localDataSource, remoteDataSource)
    }
}

