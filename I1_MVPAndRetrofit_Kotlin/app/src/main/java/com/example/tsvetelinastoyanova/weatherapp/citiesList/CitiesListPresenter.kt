package com.example.tsvetelinastoyanova.weatherapp.citiesList

import android.os.Handler
import com.example.tsvetelinastoyanova.weatherapp.City
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.util.convertCityEntityToCity

class CitiesListPresenter(private val view: CitiesListContract.View,
                          private val citiesRepository: CitiesRepository) : CitiesListContract.Presenter {

    override fun start() {
        loadCities()
        setTimerToRefresh()
    }

    override fun loadCities() {
        citiesRepository.getCities(object : CityDataSource.GetCityCallback {
            override fun onCityLoaded(cityEntity: CityEntity) {
                view.showCityLoaded(convertCityEntityToCity(cityEntity))
            }

            override fun onCityDoesNotExist() {
                view.showErrorLoadingCities()
            }
        })
    }

    override fun addNewCity(cityName: String?) {
        if (cityName == null) {
            view.showErrorNotValidCityName()
        } else {
            citiesRepository.addNotAddedCity(cityName, object : LocalDataSource.AddCityCallback {
                override fun onCityAddedSuccessfully(cityEntity: CityEntity) {
                    view.showNewCityAdded(convertCityEntityToCity(cityEntity))
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

    override fun getWeatherObjectOnClick(cityName: String) =
        citiesRepository.getWeatherObjectWithName(cityName)


    override fun refreshCities(cities: MutableList<City>?) {
        cities?.let {
            citiesRepository.refreshCities(cities, object : CityDataSource.GetCityCallback {
                override fun onCityLoaded(cityEntity: CityEntity) {
                    view.showCityUpdated(convertCityEntityToCity(cityEntity))
                }

                override fun onCityDoesNotExist() {
                    view.showErrorLoadingCities()
                }
            })
        }
    }

    private fun setTimerToRefresh() = Handler().postDelayed({
        refreshCities(view.getDisplayedCities())
    }, 900000)

}