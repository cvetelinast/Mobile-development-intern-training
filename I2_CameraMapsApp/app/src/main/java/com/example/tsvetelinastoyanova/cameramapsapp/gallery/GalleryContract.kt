package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView

interface GalleryContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }
}