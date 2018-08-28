package com.example.tsvetelinastoyanova.hackernewsapp.data

import com.example.tsvetelinastoyanova.hackernewsapp.data.local.StoriesLocalDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesRemoteDataSourceFactory
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class StoriesRepository(private val localDataSource: StoriesLocalDataSource,
                        private val remoteDataSourceFactory: StoriesRemoteDataSourceFactory) : Repository {
   /* override fun getTopStories(): Observable<Story> {
        return remoteDataSource.getTopStories()
    }

    override fun getStoryById(id: String): Observable<Story> {
        return remoteDataSource.getStoryById(id)
    }*/

    companion object {
        @Volatile
        private var INSTANCE: StoriesRepository? = null

        fun getInstance(localDataSource: StoriesLocalDataSource,
                        remoteDataSourceFactory: StoriesRemoteDataSourceFactory): StoriesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: StoriesRepository(localDataSource, remoteDataSourceFactory).also { INSTANCE = it }
            }
        }
    }
}