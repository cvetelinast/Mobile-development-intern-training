package com.example.tsvetelinastoyanova.hackernewsapp.topnews

import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BaseView

interface TopNewsContract {

    interface View: BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {


    }
}