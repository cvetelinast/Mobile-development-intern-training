package com.example.tsvetelinastoyanova.weatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.WeatherObject
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.WeatherDetailsFragment


class WeatherDetailsActivity : AppCompatActivity() {
    internal var weatherObject: CurrentWeatherObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)
        val intent = intent
        if (savedInstanceState == null) {
            val newFragment = WeatherDetailsFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, newFragment).commit()
            val weatherObject: CurrentWeatherObject? = intent.getParcelableExtra(Constants.WEATHER_OBJECTS)
            this.weatherObject = weatherObject
        }
    }

    override fun onStart() {
        super.onStart()
        weatherObject?.let {
            val details = supportFragmentManager.findFragmentById(R.id.fragment_container) as? WeatherDetailsFragment
            details?.changeView(it)
        }
    }
}