package com.example.tsvetelinastoyanova.weatherapp.citiesList

import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView
import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject

interface CitiesListContract {
    interface View : BaseView<Presenter> {

        fun showNewCityAdded(newCity: City)

        fun showErrorAddingAddedCity()

        fun showErrorLoadingCities()

        fun showCityDeleted()

        fun showErrorDeleteCity()

        fun showErrorNotValidCityName()

        fun showCityLoaded(city: City)

        fun showCityUpdated(city: City)

        fun setWeatherObjectWhenClicked(cityName: String)

        fun getDisplayedCities() : MutableList<City>?
    }

    interface Presenter : BasePresenter {
        fun loadCities()

        fun addNewCity(s: String?)

        //    void updateCity(CityEntity s);

        fun deleteCity(city: City)

        fun refreshCities(cities: MutableList<City>?)

        fun getWeatherObjectOnClick(cityName: String): CurrentWeatherObject?


        //    void addNewWeatherObject(WeatherObject weatherObject);
    }
}