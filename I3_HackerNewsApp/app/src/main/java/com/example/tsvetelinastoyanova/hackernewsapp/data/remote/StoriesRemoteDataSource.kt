package com.example.tsvetelinastoyanova.hackernewsapp.data.remote

import com.example.tsvetelinastoyanova.hackernewsapp.data.Repository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class StoriesRemoteDataSource : Repository {
    companion object {

        private var INSTANCE: StoriesRemoteDataSource? = null

        fun getInstance(): StoriesRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(StoriesRemoteDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = StoriesRemoteDataSource()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getTopStories(): Observable<List<Int>> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getTopStories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}