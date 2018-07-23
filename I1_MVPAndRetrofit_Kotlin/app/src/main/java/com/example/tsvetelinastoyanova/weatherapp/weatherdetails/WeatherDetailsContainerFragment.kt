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
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast.ForecastFragment
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast.ForecastPresenter
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.weather.WeatherDetailsPresenter

class WeatherDetailsContainerFragment : Fragment(), WeatherDetailsFragment.OnLoadedChildFragmentDelegate {
    // private var citiesRepository: CitiesRepository? = null

    var currentWeatherObject: CurrentWeatherObject? = null
    var weatherDetailsFragment: WeatherDetailsFragment? = null
    var forecastFragment: ForecastFragment? = null
    private var areChildrenLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather_details_container, container, false)
        view.toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp)
        view.toolbar.setNavigationOnClickListener({ _ -> activity?.onBackPressed() })

        val viewPager = view.viewpager
        setupViewPager(viewPager)

        val tabLayout = view.tabs
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    companion object {
        fun newInstance(): WeatherDetailsContainerFragment {
            return WeatherDetailsContainerFragment()
        }
    }

    override fun onLoadedWeatherFragment() {
        weatherDetailsFragment?.apply {
            areChildrenLoaded = true
            currentWeatherObject?.let {
                this.changeView(it)
                forecastFragment?.setCityName(it.name)
            }
        }
    }

    fun clickOnCity(weatherObject: CurrentWeatherObject) {
        currentWeatherObject = weatherObject
        if (areChildrenLoaded) {
            weatherDetailsFragment?.apply {
                currentWeatherObject?.let {
                    areChildrenLoaded = true
                    this.changeView(it)
                    forecastFragment?.setCityName(it.name)
                }
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val activity = requireActivity()

        val adapter = ViewPagerAdapter(activity.supportFragmentManager)
        setupWeatherDetailsFragment(adapter)

        val citiesRepository = Utils.provideCityRepository(activity)

        forecastFragment = ForecastFragment().apply {
            val presenter = ForecastPresenter(this, citiesRepository)
            setPresenter(presenter)
            adapter.addFragment(this, "Forecast")
        }

        viewPager.adapter = adapter
    }

    private fun setupWeatherDetailsFragment(adapter: ViewPagerAdapter) {
        weatherDetailsFragment = WeatherDetailsFragment()
        weatherDetailsFragment?.let {
            it.setPresenter(WeatherDetailsPresenter())
            it.setDelegate(this)
            adapter.addFragment(it, "Weather details")
        }
    }


    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val fragmentList = ArrayList<Fragment>()
        private val fragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

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
