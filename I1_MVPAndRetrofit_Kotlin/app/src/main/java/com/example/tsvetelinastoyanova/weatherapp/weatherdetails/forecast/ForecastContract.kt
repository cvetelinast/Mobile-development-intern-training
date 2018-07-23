package com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast

import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView

interface ForecastContract {

    interface View : BaseView<Presenter> {
        fun showErrorLoadingForecast()
    }

    interface Presenter : BasePresenter {
        fun startGettingForecastForCity(city: String)
    }
}