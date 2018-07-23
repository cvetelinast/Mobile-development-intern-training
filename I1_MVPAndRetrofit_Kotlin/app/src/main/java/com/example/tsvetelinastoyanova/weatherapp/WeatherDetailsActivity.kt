package com.example.tsvetelinastoyanova.weatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.WeatherDetailsContainerFragment


class WeatherDetailsActivity : AppCompatActivity() {
    internal var weatherObject: CurrentWeatherObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)
        val intent = intent
        if (savedInstanceState == null) {
            val newFragment = WeatherDetailsContainerFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, newFragment).commit()
            val weatherObject: CurrentWeatherObject? = intent.getParcelableExtra(Constants.WEATHER_OBJECTS)
            this.weatherObject = weatherObject
        }
    }

    override fun onStart() {
        super.onStart()
        Utils.checkNotNull(weatherObject)
        weatherObject?.let {
            val details = supportFragmentManager.findFragmentById(R.id.fragment_container) as? WeatherDetailsContainerFragment
            details?.clickOnCity(it)
        }
    }
}