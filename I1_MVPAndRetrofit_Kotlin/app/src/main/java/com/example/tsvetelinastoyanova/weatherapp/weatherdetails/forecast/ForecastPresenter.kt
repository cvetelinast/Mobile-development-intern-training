package com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast

import android.util.Log
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository

class ForecastPresenter(private val view: ForecastContract.View, private val citiesRepository: CitiesRepository)
    : ForecastContract.Presenter {

    override fun start() {
        Log.d("tag", "started forecast fragment presenter")
    }

    override fun startGettingForecastForCity(city: String) {
        val result = citiesRepository.getForecastForCity(city)
                .subscribe(
                        { forecastObject -> Log.d("tag", "YGSDFGS Forecast object: $forecastObject") },
                        { error -> view.showErrorLoadingForecast() }
                )
    }
}