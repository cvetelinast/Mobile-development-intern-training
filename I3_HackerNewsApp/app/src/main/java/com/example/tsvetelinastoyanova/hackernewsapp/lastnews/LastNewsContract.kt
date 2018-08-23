package com.example.tsvetelinastoyanova.hackernewsapp.lastnews

import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.hackernewsapp.abstraction.BaseView

interface LastNewsContract {


    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {


    }
}