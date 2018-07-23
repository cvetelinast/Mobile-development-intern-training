package com.example.tsvetelinastoyanova.weatherapp.weatherdetails.weather

import android.util.Log
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.weather.WeatherDetailsContract

class WeatherDetailsPresenter : WeatherDetailsContract.Presenter {
    override fun start() {
        Log.d("tag", "WeatherDetailsPresenter started")
    }
}