package com.example.tsvetelinastoyanova.hackernewsapp.favouritenews

import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BaseView

interface FavouriteNewsContract {
    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {


    }
}