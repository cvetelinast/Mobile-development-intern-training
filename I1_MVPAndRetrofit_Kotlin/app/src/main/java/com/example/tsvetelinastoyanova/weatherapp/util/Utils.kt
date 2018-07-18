package com.example.tsvetelinastoyanova.weatherapp.util

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.tsvetelinastoyanova.weatherapp.R
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.AppDatabase
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.CitiesLocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.remote.CitiesRemoteDataSource
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.Main


object Utils {
    fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }

    fun provideCityRepository(context: Context): CitiesRepository? {
        checkNotNull(context)
        val database = AppDatabase.getInstance(context)

        val localDataSource: CitiesLocalDataSource? = CitiesLocalDataSource.getInstance(AppExecutors(), database.cityDao())
        val remoteDataSource: CitiesRemoteDataSource? = CitiesRemoteDataSource.getInstance()
        localDataSource?.apply {
            remoteDataSource?.let {
                return CitiesRepository.getInstance(this, it)
            }
        }
        return null
    }

    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    fun <T> checkNotNull(reference: T?): T {
        if (reference == null) {
            throw NullPointerException()
        }
        return reference
    }

    fun convertToWeatherObjectWithCelsiusTemperature(c: CurrentWeatherObject): CurrentWeatherObject {
        val main = Main(temp = convertTemperatureToCelsius(c.main.temp), pressure = c.main.pressure, humidity = c.main.humidity,
                tempMin = convertTemperatureToCelsius(c.main.tempMin), tempMax = convertTemperatureToCelsius(c.main.tempMax))
        return CurrentWeatherObject(coord = c.coord, weather = c.weather, base = c.base, main = main, visibility = c.visibility,
                wind = c.wind, clouds = c.clouds, dt = c.dt, sys = c.sys, id = c.id, name = c.name, cod = c.cod)
    }

    private fun convertTemperatureToCelsius(temp: Double): Double {
        return Math.round((temp - 273.15) * 10) / 10.0
    }
}