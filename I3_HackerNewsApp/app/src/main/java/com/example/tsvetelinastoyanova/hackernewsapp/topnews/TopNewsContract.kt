package com.example.tsvetelinastoyanova.hackernewsapp.topnews

import android.arch.paging.PagedList
import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BaseView
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

interface TopNewsContract {

    interface View : BaseView<Presenter> {

    //    fun showStoryInRecyclerView(story: Story)

        fun showProgressBar()

        fun hideProgressBar()

        fun submitList(list: PagedList<Story>?)

     //   fun setNetworkStateToAdapter(networkState: NetworkState?)
    }

    interface Presenter : BasePresenter {

     /*   fun retry()*/

    }
}