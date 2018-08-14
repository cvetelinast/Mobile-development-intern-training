package com.example.tsvetelinastoyanova.cameramapsapp.maps

import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView

interface MapsContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {
    }
}