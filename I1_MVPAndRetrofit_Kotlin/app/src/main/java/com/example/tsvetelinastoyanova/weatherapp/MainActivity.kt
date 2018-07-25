package com.example.tsvetelinastoyanova.weatherapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.tsvetelinastoyanova.weatherapp.citiesList.CitiesListFragment
import com.example.tsvetelinastoyanova.weatherapp.citiesList.CitiesListPresenter
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.WeatherDetailsContainerFragment
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.WeatherDetailsContainerPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), CitiesListFragment.OnClickCityDelegate {

    private var weatherDetailsContainerFragment: WeatherDetailsContainerFragment? = null
    private var weatherObject: CurrentWeatherObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherObject = savedInstanceState?.getParcelable(Constants.WEATHER_OBJECTS)

        if (list_fragment_container != null) {
            createCitiesListFragment()
        }
        if (details_fragment_container != null) {
            createWeatherDetailsFragment()
        }
    }

    override fun onClickCity(currentWeatherObject: CurrentWeatherObject) {
        if (Utils.isTablet(this)) {
            weatherObject = currentWeatherObject
            weatherDetailsContainerFragment?.clickOnCity(currentWeatherObject, true)
        } else {
            val i = Intent(this@MainActivity, WeatherDetailsActivity::class.java)
            i.putExtra(Constants.WEATHER_OBJECTS, currentWeatherObject)
            startActivity(i)
        }
    }

    private fun createCitiesListFragment() {
        var citiesListFragment: CitiesListFragment? = supportFragmentManager.findFragmentById(R.id.list_fragment_container) as? CitiesListFragment
        if (citiesListFragment == null) {
            citiesListFragment = CitiesListFragment.newInstance()
            Utils.addFragmentToActivity(
                supportFragmentManager, citiesListFragment, R.id.list_fragment_container)
        }
        val citiesRepository: CitiesRepository? = Utils.provideCityRepository(applicationContext)
        citiesRepository?.let {
            val citiesListPresenter = CitiesListPresenter(citiesListFragment, it)
            citiesListFragment.setPresenter(citiesListPresenter)
        }
    }

    private fun createWeatherDetailsFragment() {
        var weatherDetailsContainerFragment: WeatherDetailsContainerFragment? = supportFragmentManager.findFragmentById(R.id.details_fragment_container) as? WeatherDetailsContainerFragment
        if (weatherDetailsContainerFragment == null) {
            weatherDetailsContainerFragment = WeatherDetailsContainerFragment.newInstance()
            Utils.addFragmentToActivity(
                supportFragmentManager, weatherDetailsContainerFragment, R.id.details_fragment_container)
        }
        val weatherDetailsContainerPresenter = WeatherDetailsContainerPresenter(weatherDetailsContainerFragment, Utils.provideCityRepository(this))
        weatherDetailsContainerFragment.setPresenter(weatherDetailsContainerPresenter)
        this.weatherDetailsContainerFragment = weatherDetailsContainerFragment

        weatherObject?.let {
            onClickCity(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run { putParcelable(Constants.WEATHER_OBJECTS, weatherObject) }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        weatherObject = savedInstanceState?.getParcelable(Constants.WEATHER_OBJECTS)
    }
}
