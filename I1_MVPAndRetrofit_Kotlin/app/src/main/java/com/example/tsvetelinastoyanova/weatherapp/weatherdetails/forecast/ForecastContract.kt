package com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast

import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject

interface ForecastContract {

    interface View : BaseView<Presenter> {

        fun showForecastForCity(forecastObject: ForecastObject)
    }

    interface Presenter : BasePresenter {
    }
}