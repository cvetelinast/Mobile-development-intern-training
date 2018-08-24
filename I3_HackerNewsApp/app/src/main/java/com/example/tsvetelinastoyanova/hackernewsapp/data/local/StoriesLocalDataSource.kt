package com.example.tsvetelinastoyanova.hackernewsapp.data.local

class StoriesLocalDataSource {

    companion object {

        private var INSTANCE: StoriesLocalDataSource? = null

        fun getInstance(/*cityDao: CityDao*/): StoriesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(StoriesLocalDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoriesLocalDataSource(/*cityDao*/)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}