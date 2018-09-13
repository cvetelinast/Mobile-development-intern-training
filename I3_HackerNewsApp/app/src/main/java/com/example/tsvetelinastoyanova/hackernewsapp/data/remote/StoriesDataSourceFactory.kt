package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import android.arch.paging.DataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.filtereddatasources.FilteredDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesdatasources.NewStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesdatasources.TopStoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

class StoriesDataSourceFactory(private val typeRemoteDataSource: TypeRemoteDataSource,
                               private val query: String) : DataSource.Factory<Long, Story>() {

    override fun create(): DataSource<Long, Story> {
        return when (typeRemoteDataSource) {
            TypeRemoteDataSource.TOP_STORIES -> TopStoriesRemoteDataSource.getInstance()
            TypeRemoteDataSource.NEW_STORIES -> NewStoriesRemoteDataSource.getInstance()
            TypeRemoteDataSource.FILTERED_STORIES -> FilteredDataSource(query)
        }
    }
}