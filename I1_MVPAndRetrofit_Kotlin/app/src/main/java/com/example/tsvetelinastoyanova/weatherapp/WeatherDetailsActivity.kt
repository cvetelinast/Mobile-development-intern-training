package com.example.tsvetelinastoyanova.weatherapp

import android.app.FragmentTransaction
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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

        /*val temp = supportFragmentManager.findFragmentById(R.id.fragment_container) as? WeatherDetailsContainerFragment
        supportFragmentManager.beginTransaction().remove(temp).commit()*/

        var tempFragment: WeatherDetailsContainerFragment? = supportFragmentManager.findFragmentById(R.id.fragment_container) as? WeatherDetailsContainerFragment
        if (tempFragment == null) {
            //Utils.removeFragment(supportFragmentManager, tempFragment)

            weatherDetailsContainerFragment = WeatherDetailsContainerFragment()/*.newInstance()*/
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, weatherDetailsContainerFragment).commit()
        } else {
            weatherDetailsContainerFragment = tempFragment
        }

        Log.d("tag", "Creating new weatherDetailsContainerFragment")
       /* weatherDetailsContainerFragment = WeatherDetailsContainerFragment()*//*.newInstance()*//*
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, weatherDetailsContainerFragment).commit()*/

        if (this.weatherObject == null) {
            val weatherObject: CurrentWeatherObject? = intent.getParcelableExtra(Constants.WEATHER_OBJECTS)
            this.weatherObject = weatherObject
        }
        Utils.checkNotNull(weatherDetailsContainerFragment)

        weatherDetailsContainerFragment.apply {
            val weatherDetailsContainerPresenter = WeatherDetailsContainerPresenter(this, Utils.provideCityRepository(this@WeatherDetailsActivity))
            Log.d("tag", "Set presenter")
            this.setPresenter(weatherDetailsContainerPresenter)
            weatherObject?.let {
                this.clickOnCity(it, false)
            }
        }
        //  containerId = savedInstanceState?.getInt(CONTAINER_FRAGMENT_ID) ?: 0


        //  }
    }

    override fun onStart() {
        super.onStart()
        //    Utils.checkNotNull(weatherObject)
        /*weatherObject?.let {

            val weatherDetailsContainerFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? WeatherDetailsContainerFragment
            Utils.checkNotNull(weatherDetailsContainerFragment)
            weatherDetailsContainerFragment?.apply {
                val weatherDetailsContainerPresenter = WeatherDetailsContainerPresenter(this, Utils.provideCityRepository(this@WeatherDetailsActivity))
                this.setPresenter(weatherDetailsContainerPresenter)
                this.clickOnCity(it, false)
            }
        }*/
    }


    override fun onResume() {
        super.onResume()
        /* Utils.checkNotNull(weatherObject)
         weatherObject?.let {

             *//* if (containerId != 0) {
                 weatherDetailsContainerFragment = supportFragmentManager.findFragmentById(containerId) as? WeatherDetailsContainerFragment

             } else {
                 weatherDetailsContainerFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? WeatherDetailsContainerFragment*//*
            *//*Utils.checkNotNull(weatherDetailsContainerFragment)
            weatherDetailsContainerFragment?.apply {
                val weatherDetailsContainerPresenter = WeatherDetailsContainerPresenter(this, Utils.provideCityRepository(this@WeatherDetailsActivity))
                this.setPresenter(weatherDetailsContainerPresenter)
                this.clickOnCity(it, false)
            }*//*
        }*/
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
      //  super.onSaveInstanceState(outState, outPersistentState)
        outState?.run {
            putParcelable(Constants.WEATHER_OBJECTS, weatherObject)
            //putInt(CONTAINER_FRAGMENT_ID, containerId)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
     //   super.onRestoreInstanceState(savedInstanceState)
        weatherObject = savedInstanceState?.getParcelable(Constants.WEATHER_OBJECTS)
        //containerId = savedInstanceState?.getInt(CONTAINER_FRAGMENT_ID) ?: 0
    }
}