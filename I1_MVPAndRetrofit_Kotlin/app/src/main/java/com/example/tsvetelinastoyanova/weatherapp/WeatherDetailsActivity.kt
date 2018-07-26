package com.example.tsvetelinastoyanova.weatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.WeatherDetailsContainerFragment
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.WeatherDetailsContainerPresenter

class WeatherDetailsActivity : AppCompatActivity() {
    private var weatherObject: CurrentWeatherObject? = null
    private lateinit var weatherDetailsContainerFragment: WeatherDetailsContainerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        initWeatherObject()
        setupContainer()
    }

    private fun initWeatherObject() {
        if (this.weatherObject == null) {
            val weatherObject: CurrentWeatherObject? = intent.getParcelableExtra(Constants.WEATHER_OBJECTS)
            this.weatherObject = weatherObject
        }
    }

    private fun setupContainer() {
        val tempFragment: WeatherDetailsContainerFragment? =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as? WeatherDetailsContainerFragment
        if (tempFragment == null) {
            weatherDetailsContainerFragment = WeatherDetailsContainerFragment.newInstance()
            Utils.addFragmentToActivity(supportFragmentManager, weatherDetailsContainerFragment, R.id.fragment_container)
        } else {
            weatherDetailsContainerFragment = tempFragment
        }
        setupContainerDetails()
    }

    private fun setupContainerDetails() {
        val weatherDetailsContainerPresenter = WeatherDetailsContainerPresenter(weatherDetailsContainerFragment,
            Utils.provideCityRepository(this))

        weatherDetailsContainerFragment.setPresenter(weatherDetailsContainerPresenter)
        weatherObject?.let {
            weatherDetailsContainerFragment.clickOnCity(it, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run { putParcelable(Constants.WEATHER_OBJECTS, weatherObject) }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        weatherObject = savedInstanceState?.getParcelable(Constants.WEATHER_OBJECTS)
    }
}