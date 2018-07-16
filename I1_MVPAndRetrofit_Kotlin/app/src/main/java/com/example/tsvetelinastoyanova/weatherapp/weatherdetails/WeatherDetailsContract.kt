package com.example.tsvetelinastoyanova.weatherapp.weatherdetails

import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject

interface WeatherDetailsContract {

    interface View : BaseView<Presenter> {
        fun changeView(currentWeatherObject: CurrentWeatherObject)
    }

    interface Presenter : BasePresenter
}