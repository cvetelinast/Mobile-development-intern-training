package com.example.tsvetelinastoyanova.hackernewsapp.common

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.BaseSchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesDataSourceFactory
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.TypeRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import com.example.tsvetelinastoyanova.hackernewsapp.recyclerview.New
import io.reactivex.Observable
import java.text.SimpleDateFormat

object Utils {

    private val PATTERN_DATETIME_FORMAT = "MM dd, yyyy hh:mma"
    private val pageSize = 15

    /* fun switchFragment(fragmentManager: FragmentManager,
                        newFragment: Fragment, frameId: Int, nameOfFragment: String) {
         checkNotNull(fragmentManager)
         checkNotNull(newFragment)
         val transaction = fragmentManager.beginTransaction()
         transaction.replace(frameId, newFragment, nameOfFragment)
         transaction.commit()
     }*/

    fun addFragment(fragmentManager: FragmentManager,
                    newFragment: Fragment, frameId: Int, nameOfFragment: String) {
        checkNotNull(fragmentManager)
        checkNotNull(newFragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, newFragment, nameOfFragment)
        transaction.commit()
    }

    /*  fun provideRepository(baseSchedulerProvider: BaseSchedulerProvider): StoriesRepository {
          val localDataSource = StoriesLocalDataSource.getInstance(baseSchedulerProvider)
          val remoteDataSourceFactory = TopStoriesRepositoryFactory()
          return StoriesRepository(*//*localDataSource, remoteDataSourceFactory*//*)
    }*/

    /*  fun provideNewStoriesRepository(baseSchedulerProvider: BaseSchedulerProvider): StoriesRepository {
          val localDataSource = StoriesLocalDataSource.getInstance(baseSchedulerProvider)
          val remoteDataSourceFactory = TopStoriesRepositoryFactory()
          return StoriesRepository(*//*localDataSource, remoteDataSourceFactory*//*)
    }*/

    fun provideStoriesObservable(typeRemoteDataSource: TypeRemoteDataSource,
                                    baseSchedulerProvider: BaseSchedulerProvider): Observable<PagedList<Story>> {
        val sourceFactory = StoriesDataSourceFactory(typeRemoteDataSource)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()

        return RxPagedListBuilder<Long, Story>(sourceFactory, config)
            .setFetchScheduler(baseSchedulerProvider.io())
            .setNotifyScheduler(baseSchedulerProvider.ui())
            .buildObservable()
    }


   /* fun provideNewStoriesObservable(typeRemoteDataSource: TypeRemoteDataSource, baseSchedulerProvider: BaseSchedulerProvider): Observable<PagedList<Story>> {
        val sourceFactory = StoriesDataSourceFactory(TypeRemoteDataSource.NEW_STORIES)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()

        return RxPagedListBuilder<Long, Story>(sourceFactory, config)
            .setFetchScheduler(baseSchedulerProvider.io())
            .setNotifyScheduler(baseSchedulerProvider.ui())
            .buildObservable()
    }

    fun provideTopStoriesObservable(baseSchedulerProvider: BaseSchedulerProvider): Observable<PagedList<Story>> {
        val sourceFactory = StoriesDataSourceFactory(TypeRemoteDataSource.TOP_STORIES)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()

        return RxPagedListBuilder<Long, Story>(sourceFactory, config)
            .setFetchScheduler(baseSchedulerProvider.io())
            .setNotifyScheduler(baseSchedulerProvider.ui())
            .buildObservable()
    }*/

    fun convertStoryToNew(story: Story): New = New(story.title, story.score.toString(), convertUnixTimeToDatetime(story.time.toString()))

    private fun convertUnixTimeToDatetime(time: String): String {
        val dv = java.lang.Long.valueOf(time) * 1000
        val df = java.util.Date(dv)
        return SimpleDateFormat(PATTERN_DATETIME_FORMAT).format(df)
    }
}
