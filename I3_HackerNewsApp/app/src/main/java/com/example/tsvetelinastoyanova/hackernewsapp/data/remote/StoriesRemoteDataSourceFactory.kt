package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import android.arch.paging.DataSource
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

class StoriesRemoteDataSourceFactory : DataSource.Factory<Long, Story>() {

    override fun create(): DataSource<Long, Story> {
        return StoriesRemoteDataSource()
    }
}