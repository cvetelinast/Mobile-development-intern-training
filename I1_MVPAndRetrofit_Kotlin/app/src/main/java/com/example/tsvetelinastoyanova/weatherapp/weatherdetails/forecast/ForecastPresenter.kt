package com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast

import android.util.Log

class ForecastPresenter(private val view: ForecastContract.View)
    : ForecastContract.Presenter {

    override fun start() {

        Log.d("tag", "started forecast fragment presenter")
    }
}