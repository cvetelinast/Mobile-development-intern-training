package com.example.tsvetelinastoyanova.cameramapsapp.camera

import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView

interface CameraContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }
}