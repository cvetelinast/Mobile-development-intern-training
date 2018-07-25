package com.example.tsvetelinastoyanova.weatherapp.weatherdetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tsvetelinastoyanova.weatherapp.R
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import kotlinx.android.synthetic.main.fragment_weather_details_container.view.*
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.Toast
import com.example.tsvetelinastoyanova.weatherapp.Constants
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.ForecastObject
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast.ForecastFragment
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast.ForecastPresenter
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.weather.WeatherDetailsPresenter

class WeatherDetailsContainerFragment : Fragment(), WeatherDetailsContainerContract.View, WeatherDetailsFragment.OnLoadedWeatherFragmentDelegate, ForecastFragment.OnLoadedForecastFragmentDelegate {

    private lateinit var presenter: WeatherDetailsContainerContract.Presenter
    private var currentWeatherObject: CurrentWeatherObject? = null
    private lateinit var weatherDetailsFragment: WeatherDetailsFragment/*? = null*/
    private /*lateinit */var forecastFragment: ForecastFragment? = null

    private var shouldCreateFragments = true

    private var isWeatherFragmentLoaded: Boolean = false
    private var isForecastFragmentLoaded: Boolean = false


    /*** Methods from Contract ***/
    override fun showErrorLoadingForecast() {
        Toast.makeText(context, R.string.problem_loading_forecast, Toast.LENGTH_LONG).show()
    }

    override fun showSuccessfullyLoadedForecast(forecastObject: ForecastObject) {
        Utils.checkNotNull(forecastObject)
        Utils.checkNotNull(forecastFragment)
        forecastFragment?.showForecastForCity(forecastObject)
    }

    /*** Fragment methods ***/
    override fun setPresenter(presenter: WeatherDetailsContainerContract.Presenter) {
        Utils.checkNotNull(presenter)
        this.presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d("tag", "Container onCreateView()")
        val view = inflater.inflate(R.layout.fragment_weather_details_container, container, false)
        view.toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp)
        view.toolbar.setNavigationOnClickListener({ _ -> activity?.onBackPressed() })

        Log.d("tag", "FRAGMENTS SIZE: ${activity?.supportFragmentManager?.fragments?.size}")

        val viewPager = view.viewpager
        setupViewPager(viewPager)

        val tabLayout = view.tabs
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        fun newInstance(): WeatherDetailsContainerFragment {
            return WeatherDetailsContainerFragment()
        }
    }

    /*** Communication with WeatherDetailsFragment ***/
    override fun onLoadedWeatherFragment() {
        Utils.checkNotNull(presenter)
        isWeatherFragmentLoaded = true

        currentWeatherObject?.let {
            isWeatherFragmentLoaded = true
            weatherDetailsFragment.changeView(it)
        }
    }

    /*** Communication with ForecastFragment ***/
    override fun onLoadedForecastFragment() {
        Utils.checkNotNull(presenter)
        isForecastFragmentLoaded = true

        currentWeatherObject?.name?.let {
            presenter.startGettingForecastForCity(it)
        }
    }

    fun clickOnCity(weatherObject: CurrentWeatherObject, isTabletMode: Boolean) {
        // for tablet
        currentWeatherObject = weatherObject
        if (isTabletMode && isWeatherFragmentLoaded && isForecastFragmentLoaded) {
            currentWeatherObject?.let {
                weatherDetailsFragment.changeView(it)
                presenter.startGettingForecastForCity(it.name)
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val activity = requireActivity()
        val adapter = ViewPagerAdapter(activity.supportFragmentManager)
        adapter.init()
        setupWeatherDetailsFragment(adapter)
        setupForecastFragment(adapter)
        viewPager.adapter = adapter
    }

    private fun setupWeatherDetailsFragment(adapter: ViewPagerAdapter) {
        if (shouldCreateFragments/*adapter.count == 0*/) {
            weatherDetailsFragment = WeatherDetailsFragment()
            weatherDetailsFragment.setPresenter(WeatherDetailsPresenter())
            weatherDetailsFragment.setWeatherDelegate(this)
            adapter.addFragment(weatherDetailsFragment, Constants.WEATHER_DETAILS_FRAGMENT)
        } else {
            weatherDetailsFragment = adapter.getItem(0) as WeatherDetailsFragment
            weatherDetailsFragment.setPresenter(WeatherDetailsPresenter())
            weatherDetailsFragment.setWeatherDelegate(this)
        }
    }

    private fun setupForecastFragment(adapter: ViewPagerAdapter) {
        if (shouldCreateFragments/*adapter.count <= 1*/) {
            forecastFragment = ForecastFragment.newInstance()
            forecastFragment?.let {
                val presenter = ForecastPresenter(it)
                it.setPresenter(presenter)
                it.setForecastDelegate(this)
                adapter.addFragment(it, Constants.FORECAST_FRAGMENT)
            }
        } else {
            forecastFragment = adapter.getItem(1) as ForecastFragment
            forecastFragment?.let {
                val presenter = ForecastPresenter(it)
                it.setPresenter(presenter)
                it.setForecastDelegate(this)
            }
        }
        //   forecastFragment = ForecastFragment.newInstance()

    }

    override fun onDetach() {
        super.onDetach()
    }

    internal inner class ViewPagerAdapter(val manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val fragmentList = ArrayList<Fragment>()
        private val fragmentTitleList = ArrayList<String>()

        fun init() {
            val countFragments = manager.fragments.size
            if (countFragments > 2) {
                this.addFragment(manager.fragments[countFragments - 2] as WeatherDetailsFragment, Constants.WEATHER_DETAILS_FRAGMENT)
                this.addFragment(manager.fragments[countFragments - 1] as ForecastFragment, Constants.FORECAST_FRAGMENT)
                shouldCreateFragments = false
            }
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        /* fun getItemWithTitle(title: String): Fragment {
             for(i in fragmentList.indices){
                 val currentTitle = getPageTitle(i) ?: ""
                 if(currentTitle == title){
                     return fragmentList[i]
                 }
             }
         }*/

        fun addFragment(fragment: WeatherDetailsFragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        fun addFragment(fragment: ForecastFragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }
    }
}
