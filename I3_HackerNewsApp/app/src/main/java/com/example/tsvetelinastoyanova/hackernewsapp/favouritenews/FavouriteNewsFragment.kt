package com.example.tsvetelinastoyanova.hackernewsapp.favouritenews

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.hackernewsapp.R

class FavouriteNewsFragment : Fragment(), FavouriteNewsContract.View {

    private var presenter: FavouriteNewsContract.Presenter? = null

    override fun setPresenter(presenter: FavouriteNewsContract.Presenter) {
        this.presenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourite_news, container, false)
    }

    companion object {
        fun newInstance() = FavouriteNewsFragment()
    }
}