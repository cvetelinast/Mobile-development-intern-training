package com.example.tsvetelinastoyanova.weatherapp.weatherdetails

import android.util.Log

class WeatherDetailsPresenter : WeatherDetailsContract.Presenter {
    override fun start() {
        Log.d("tag", "WeatherDetailsPresenter started")
    }
}