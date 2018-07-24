package com.example.tsvetelinastoyanova.weatherapp.weatherdetails

import android.util.Log
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository

class WeatherDetailsContainerPresenter(private val view: WeatherDetailsContainerContract.View,
                                       private val citiesRepository: CitiesRepository)
    : WeatherDetailsContainerContract.Presenter {

    override fun startGettingForecastForCity(city: String) {
        citiesRepository.let {
            it.getForecastForCity(city)
                .subscribe(
                    { forecastObject -> view.showSuccessfullyLoadedForecast(forecastObject) },
                    { _ -> view.showErrorLoadingForecast() }
                )
        }
    }

    override fun start() {
        Log.d("tag", "started Container presenter")
    }
}