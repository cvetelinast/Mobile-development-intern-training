package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import android.arch.paging.DataSource
import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.BaseSchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

class StoriesRemoteDataSourceFactory(private val schedulerProvider: BaseSchedulerProvider)
    : DataSource.Factory<Long, Story>() {

    override fun create(): DataSource<Long, Story> {
        return StoriesRemoteDataSource(schedulerProvider)
    }
}