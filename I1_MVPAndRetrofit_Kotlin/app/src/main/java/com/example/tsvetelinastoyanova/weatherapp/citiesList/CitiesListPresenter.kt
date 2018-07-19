package com.example.tsvetelinastoyanova.weatherapp.citiesList

import android.os.Handler
import android.util.Log
import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.util.CityEntityAdapter


class CitiesListPresenter(private val view: CitiesListContract.View, citiesRepository: CitiesRepository) : CitiesListContract.Presenter {
    private val citiesRepository: CitiesRepository = citiesRepository

    override fun start() {
        loadCities()
        setTimerToRefresh()
    }

    override fun loadCities() {
        citiesRepository.getCities(object : CityDataSource.GetCityCallback {
            override fun onCityLoaded(cityEntity: CityEntity) {
                view.showCityLoaded(CityEntityAdapter.convertCityEntityToCity(cityEntity))
            }

            override fun onCityDoesNotExist() {
                view.showErrorLoadingCities()
            }
        })
    }

    override fun addNewCity(cityName: String?) {
        if (cityName == null) {
            cityName ?: view.showErrorNotValidCityName()
        } else {
            citiesRepository.addCity(cityName, object : LocalDataSource.AddCityCallback {
                override fun onCityAddedSuccessfully(cityEntity: CityEntity) {
                    view.showNewCityAdded(CityEntityAdapter.convertCityEntityToCity(cityEntity))
                }

                override fun onCityExistsInDatabase() {
                    view.showErrorAddingAddedCity()
                }

                override fun onNotValidCity() {
                    view.showErrorNotValidCityName()
                }
            })
        }
    }

    override fun deleteCity(city: City) {
        citiesRepository.deleteCity(city.name, object : LocalDataSource.DeleteCityCallback {
            override fun onCityDeletedSuccessfully() {
                view.showCityDeleted()
            }

            override fun onFail() {
                view.showErrorDeleteCity()
            }
        })
    }


    override fun getWeatherObjectOnClick(cityName: String): CurrentWeatherObject? {
        return citiesRepository.getWeatherObjectWithName(cityName)
    }

    override fun refreshCities(cities: MutableList<City>?) {
        cities?.let {
            citiesRepository.refreshCities(cities, object : CityDataSource.GetCityCallback {
                override fun onCityLoaded(cityEntity: CityEntity) {
                    view.showCityUpdated(CityEntityAdapter.convertCityEntityToCity(cityEntity))
                }

                override fun onCityDoesNotExist() {
                    view.showErrorLoadingCities()
                }
            })
        }
    }

    private fun setTimerToRefresh() {
        Handler().postDelayed({
            refreshCities(view.getDisplayedCities())
        }, 10000/*900000*/)
    }
}