package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import android.arch.paging.DataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.new.NewStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.top.TopStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

class StoriesDataSourceFactory(private val typeRemoteDataSource: TypeRemoteDataSource) : DataSource.Factory<Long, Story>() {

    override fun create(): DataSource<Long, Story> {
        return when (typeRemoteDataSource) {
            TypeRemoteDataSource.TOP_STORIES -> TopStoriesRemoteDataSource.getInstance()
            TypeRemoteDataSource.NEW_STORIES -> NewStoriesRemoteDataSource.getInstance()
        }
    }
}