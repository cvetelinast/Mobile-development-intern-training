package com.example.tsvetelinastoyanova.weatherapp.weatherdetails

import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject

interface WeatherDetailsContainerContract {

    interface View : BaseView<Presenter> {

        fun showErrorLoadingForecast()

        fun showSuccessfullyLoadedForecast(forecastObject: ForecastObject)
    }

    interface Presenter : BasePresenter {

        fun startGettingForecastForCity(city: String)
    }
}