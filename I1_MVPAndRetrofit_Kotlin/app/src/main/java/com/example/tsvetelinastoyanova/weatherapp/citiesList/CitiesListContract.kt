package com.example.tsvetelinastoyanova.weatherapp.citiesList

import android.support.annotation.VisibleForTesting
import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView
import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject

interface CitiesListContract {

    interface View : BaseView<Presenter> {

        fun showNewCityAdded(newCity: City)

        fun showErrorInternetConnection()

        fun showErrorAddingAddedCity()

        fun showErrorLoadingCities()

        fun showCityDeleted()

        fun showErrorDeleteCity()

        fun showErrorNotValidCityName()

        fun showCityLoaded(city: City)

        fun showCityUpdated(city: City)

        fun setWeatherObjectWhenClicked(cityName: String)

        fun getDisplayedCities(): MutableList<City>?

        @VisibleForTesting
        fun getCityFromInputField(): String
    }

    interface Presenter : BasePresenter {

        fun loadCities()

        fun addNewCity(s: String?)

        fun deleteCity(city: City)

        fun refreshCities(cities: MutableList<City>?)

        fun getWeatherObjectOnClick(cityName: String): CurrentWeatherObject?
    }
}