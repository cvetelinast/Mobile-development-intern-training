package com.example.tsvetelinastoyanova.hackernewsapp.topnews

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.hackernewsapp.R
import com.example.tsvetelinastoyanova.hackernewsapp.recyclerview.New
import com.example.tsvetelinastoyanova.hackernewsapp.recyclerview.NewsAdapter
import android.support.v7.widget.DefaultItemAnimator

class TopNewsFragment : Fragment(), TopNewsContract.View {
    private var presenter: TopNewsContract.Presenter? = null

    override fun setPresenter(newPresenter: TopNewsContract.Presenter) {
        presenter = newPresenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_news, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = NewsAdapter(testProvideNews())
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        return view
    }

    private fun testProvideNews(): MutableList<New> {
        val list: MutableList<New> = mutableListOf()
        list.add(New("Title 1", "200 points", "23.08.2018"))
        list.add(New("Title 2", "5 points", "23.08.2018"))
        list.add(New("Title 3", "16 points", "23.08.2018"))
        return list
    }

    companion object {
        fun newInstance() = TopNewsFragment()
    }
}
