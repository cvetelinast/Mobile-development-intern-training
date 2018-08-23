package com.example.tsvetelinastoyanova.hackernewsapp.lastnews

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.hackernewsapp.R

class LastNewsFragment : Fragment(), LastNewsContract.View {
    private var presenter: LastNewsContract.Presenter? = null

    override fun setPresenter(presenter: LastNewsContract.Presenter) {
        this.presenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_last_news, container, false)
    }

    companion object {
        fun newInstance() = LastNewsFragment()
    }

}