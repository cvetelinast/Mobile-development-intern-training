package com.example.tsvetelinastoyanova.hackernewsapp.data.local

import com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers.BaseSchedulerProvider

class StoriesLocalDataSource(schedulerProvider: BaseSchedulerProvider) {

    companion object {

        private var INSTANCE: StoriesLocalDataSource? = null

        fun getInstance(/*cityDao: CityDao*/schedulerProvider: BaseSchedulerProvider): StoriesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(StoriesLocalDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoriesLocalDataSource(/*cityDao*/schedulerProvider)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}