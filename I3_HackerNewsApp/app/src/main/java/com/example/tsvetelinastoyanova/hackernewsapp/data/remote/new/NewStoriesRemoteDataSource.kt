package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.new

import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.GetDataService
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.RetrofitClient
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable

class NewStoriesRemoteDataSource : AbstractDataSource() {
    private val ids: MutableList<Int> = mutableListOf()
    private var lastReceivedIndex: Int = 0
    private val storiesList: MutableList<Story> = mutableListOf()

    companion object {
        private var INSTANCE: NewStoriesRemoteDataSource? = null
        fun getInstance(): NewStoriesRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(NewStoriesRemoteDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = NewStoriesRemoteDataSource()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun addStoriesToList(storiesList: List<Story>) {
        this.storiesList.addAll(storiesList)
    }

    override fun getStoriesList(): List<Story> {
        return storiesList
    }

    override fun getStoriesIds(): Observable<List<Int>> {
        val retrofit = RetrofitClient.instance
        val service = retrofit.create(GetDataService::class.java)
        return service.getNewStories()
    }

    override fun getLastIndex(): Int {
        return lastReceivedIndex
    }

    override fun increaseLastReceivedIndexWithValue(value: Int) {
        lastReceivedIndex += value
    }

    override fun getIdsSize(): Int {
        return ids.size
    }

    override fun addIdsFirstTime(listIds: List<Int>) {
        ids.addAll(listIds)
    }

    override fun getSublistOfCachedStories(fromIndex: Int, toIndex: Int): MutableList<Int> {
        return ids.subList(fromIndex, toIndex)
    }
}
