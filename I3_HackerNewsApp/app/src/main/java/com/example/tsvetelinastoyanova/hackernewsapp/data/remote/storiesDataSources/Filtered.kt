package com.example.tsvetelinastoyanova.hackernewsapp.data.remote.storiesdatasources


import android.util.Log
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.GetDataService
import com.example.tsvetelinastoyanova.hackernewsapp.data.remote.RetrofitClient
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class Filtered(private val searchedString: String) : AbstractDataSource() {

    private var lastReceivedIndex: Int = 0
    private val ids: MutableList<Int> = mutableListOf()
    private val storiesList: MutableList<Story> = mutableListOf()

    companion object {
        private var INSTANCE: Filtered? = null
        fun getInstance(searchedString: String): Filtered {
            if (INSTANCE == null) {
                synchronized(Filtered::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Filtered(searchedString)
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

    override fun getSublistOfCachedIds(fromIndex: Int, toIndex: Int): MutableList<Int> {
        return ids.subList(fromIndex, toIndex)
    }

    override fun loadStoriesFromCachedIds(sublist: MutableList<Int>, callback: LoadCallback<Story>) {
        Observable.fromIterable(sublist)
            .flatMap { id -> getStoryById(id.toString()) }
            .filter { story: Story? ->
                story?.title != null && story.type == "story" && story.title.toLowerCase().contains(searchedString.toLowerCase())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .subscribe(
                { stories ->
                    addStoriesToList(stories)
                    callback.onResult(stories)
                },
                { error -> Log.d("tag", "Error in loadAfter(): $error") }
            )
    }
}
