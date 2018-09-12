package com.example.tsvetelinastoyanova.hackernewsapp.news

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
import android.webkit.WebView
import android.widget.ProgressBar
import com.example.tsvetelinastoyanova.hackernewsapp.model.Story

class NewsFragment : Fragment(), NewsContract.View {

    private var presenter: NewsContract.Presenter? = null
    private var adapter: NewsAdapter? = null
    private var progressBar: ProgressBar? = null
    private var webView: WebView? = null

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun setPresenter(newPresenter: NewsContract.Presenter) {
        presenter = newPresenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        webView = view.findViewById(R.id.webView)
        createRecyclerView(view)
        return view
    }

    override fun onPause() {
        super.onPause()
        presenter?.stopDisposables()
    }

    override fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        checkNotNull(presenter)
        presenter?.start()
    }

    override fun submitList(list: PagedList<Story>?) {
        adapter?.submitList(list)
    }

    private fun createRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        adapter = NewsAdapter(listener = {
            it.url?.apply {
                webView?.visibility = View.VISIBLE
                webView?.loadUrl(this)
            }
        })
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }
}
