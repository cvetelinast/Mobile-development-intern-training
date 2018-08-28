package com.example.tsvetelinastoyanova.hackernewsapp.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.BaseSchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository
import com.example.tsvetelinastoyanova.hackernewsapp.data.local.StoriesLocalDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesRemoteDataSourceFactory
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import com.example.tsvetelinastoyanova.hackernewsapp.recyclerview.New
import java.text.SimpleDateFormat

object Utils {

    private val PATTERN_DATETIME_FORMAT = "MM dd, yyyy hh:mma"

    fun switchFragment(fragmentManager: FragmentManager,
                       newFragment: Fragment, frameId: Int, nameOfFragment: String) {
        checkNotNull(fragmentManager)
        checkNotNull(newFragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, newFragment, nameOfFragment)
        transaction.commit()
    }

    fun provideRepository(baseSchedulerProvider: BaseSchedulerProvider): StoriesRepository {
        val localDataSource = StoriesLocalDataSource.getInstance(baseSchedulerProvider)
        val remoteDataSourceFactory = StoriesRemoteDataSourceFactory()
        return StoriesRepository(localDataSource, remoteDataSourceFactory)
    }

    fun convertStoryToNew(story: Story): New = New(story.title, story.score.toString(), convertUnixTimeToDatetime(story.time.toString()))

    private fun convertUnixTimeToDatetime(time: String): String {
        val dv = java.lang.Long.valueOf(time) * 1000
        val df = java.util.Date(dv)
        return SimpleDateFormat(PATTERN_DATETIME_FORMAT).format(df)
    }
}
