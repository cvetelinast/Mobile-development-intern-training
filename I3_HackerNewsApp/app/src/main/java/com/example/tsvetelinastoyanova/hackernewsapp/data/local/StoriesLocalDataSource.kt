package com.example.tsvetelinastoyanova.hackernewsapp.data.local

import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.BaseSchedulerProvider
import com.example.tsvetelinastoyanova.hackernewsapp.data.StoriesRepository

class StoriesLocalDataSource(schedulerProvider: BaseSchedulerProvider) : StoriesRepository {

    companion object {

        private var INSTANCE: StoriesLocalDataSource? = null

        fun getInstance(schedulerProvider: BaseSchedulerProvider): StoriesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(StoriesLocalDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoriesLocalDataSource(schedulerProvider)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}