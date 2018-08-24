package com.example.tsvetelinastoyanova.hackernewsapp.data

import com.example.tsvetelinastoyanova.hackernewsapp.data.local.StoriesLocalDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.StoriesRemoteDataSource
import com.example.tsvetelinastoyanova.hackernewsapp.model.StoriesList
import io.reactivex.Observable

class StoriesRepository(private val localDataSource: StoriesLocalDataSource,
                        private val remoteDataSource: StoriesRemoteDataSource) : Repository {
    override fun getTopStories(): Observable<List<Int>> {
        return remoteDataSource.getTopStories()
    }

    companion object {
        @Volatile
        private var INSTANCE: StoriesRepository? = null

        fun getInstance(localDataSource: StoriesLocalDataSource,
                        remoteDataSource: StoriesRemoteDataSource): StoriesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: StoriesRepository(localDataSource, remoteDataSource).also { INSTANCE = it }
            }
        }
    }
}