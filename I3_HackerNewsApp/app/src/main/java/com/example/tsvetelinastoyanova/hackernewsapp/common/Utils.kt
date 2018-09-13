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

    const val TOP_NEWS_FRAGMENT_NAME = "TOP_NEWS_FRAGMENT"
    private const val PATTERN_DATETIME_FORMAT = "dd.MM.yyyy, hh:mma"
    const val NUM_STORIES_FOR_PAGE = 8
    const val NUM_STORIES_FOR_SEARCH = 1

    fun addFragment(fragmentManager: FragmentManager,
                    newFragment: Fragment, frameId: Int, nameOfFragment: String) {
        checkNotNull(fragmentManager)
        checkNotNull(newFragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, newFragment, nameOfFragment)
        transaction.commit()
    }

    fun provideStoriesObservable(typeRemoteDataSource: TypeRemoteDataSource,
                                 baseSchedulerProvider: BaseSchedulerProvider,
                                 query: String): Observable<PagedList<Story>> {
        val sourceFactory = StoriesDataSourceFactory(typeRemoteDataSource, query)
        val config = buildConfig()

        return RxPagedListBuilder<Long, Story>(sourceFactory, config)
            .setFetchScheduler(baseSchedulerProvider.ui())
            .setNotifyScheduler(baseSchedulerProvider.ui())
            .buildObservable()
    }

    fun convertStoryToNew(story: Story): New {
        val title = story.title ?: ""
        return New(title, story.score.toString(),
            convertUnixTimeToDatetime(story.time.toString()), story.url)
    }

    private fun buildConfig(): PagedList.Config = PagedList.Config.Builder()
        .setPageSize(NUM_STORIES_FOR_PAGE)
        .setInitialLoadSizeHint(NUM_STORIES_FOR_PAGE * 2)
        .setEnablePlaceholders(false)
        .build()

    private fun convertUnixTimeToDatetime(time: String): String {
        val timeValue = java.lang.Long.valueOf(time) * 1000
        val dateValue = java.util.Date(timeValue)
        return SimpleDateFormat(PATTERN_DATETIME_FORMAT).format(dateValue)
    }
}
