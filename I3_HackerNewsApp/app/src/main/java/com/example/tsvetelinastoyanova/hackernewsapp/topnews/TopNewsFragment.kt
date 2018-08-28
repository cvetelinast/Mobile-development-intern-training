package com.example.tsvetelinastoyanova.hackernewsapp.topnews

import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.hackernewsapp.R
import com.example.tsvetelinastoyanova.hackernewsapp.recyclerview.NewsAdapter
import android.support.v7.widget.DefaultItemAnimator
import com.example.tsvetelinastoyanova.hackernewsapp.common.Utils
import com.example.tsvetelinastoyanova.hackernewsapp.data.NetworkState
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

class TopNewsFragment : Fragment(), TopNewsContract.View {

    private var presenter: TopNewsContract.Presenter? = null
    private var adapter: NewsAdapter? = null

    override fun setPresenter(newPresenter: TopNewsContract.Presenter) {
        presenter = newPresenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_news, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        adapter = NewsAdapter()/*{ presenter.retry()}*/
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        return view
    }

    override fun showStoryInRecyclerView(story: Story) {
       // adapter?.addStory(Utils.convertStoryToNew(story))
    }

    companion object {
        fun newInstance() = TopNewsFragment()
    }

    override fun onResume() {
        super.onResume()
        checkNotNull(presenter)
        presenter?.start()
    }

    override fun submitList(list: PagedList<Story>?) {
        adapter?.submitList(list)
    }

    override fun setNetworkStateToAdapter(networkState: NetworkState?) {
        adapter?.setNetworkState(networkState)
    }
}
