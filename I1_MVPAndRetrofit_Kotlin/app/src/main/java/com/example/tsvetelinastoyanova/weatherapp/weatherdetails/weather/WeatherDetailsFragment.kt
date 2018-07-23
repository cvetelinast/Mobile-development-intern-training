package com.example.tsvetelinastoyanova.weatherapp.weatherdetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.weatherapp.Constants
import com.example.tsvetelinastoyanova.weatherapp.R
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.util.ImageOperator
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import com.example.tsvetelinastoyanova.weatherapp.weatherdetails.weather.WeatherDetailsContract
import kotlinx.android.synthetic.main.fragment_weather_details.*


class WeatherDetailsFragment : Fragment(), WeatherDetailsContract.View {

    private var presenter: WeatherDetailsContract.Presenter? = null

    /*** interface ***/

    private var onLoadedChildFragmentDelegate: OnLoadedChildFragmentDelegate? = null

    interface OnLoadedChildFragmentDelegate {
        fun onLoadedWeatherFragment()
    }

    fun setDelegate(delegate: OnLoadedChildFragmentDelegate) {
        onLoadedChildFragmentDelegate = delegate
    }

    /*** Methods from Fragment ***/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather_details, container, false)
        return view
    }

    override fun setPresenter(presenter: WeatherDetailsContract.Presenter) {
        this.presenter = presenter
    }

    override fun onResume() {
        super.onResume()
        presenter?.let {
            it.start()
        }
        Utils.checkNotNull(onLoadedChildFragmentDelegate)
        onLoadedChildFragmentDelegate?.onLoadedWeatherFragment()
    }

    override fun changeView(weatherObject: CurrentWeatherObject) {
        val city = weatherObject.name
        val country = weatherObject.sys.country
        cityAndCountry.text = String.format(Constants.COUNTRY_CITY, city, country)

        val id = ImageOperator.getImageIdFromString(weatherObject.weather[0].icon)
        icon.setImageDrawable(ContextCompat.getDrawable(requireContext(), id))

        degrees.text = String.format(Constants.DEGREES_CELSIUS, weatherObject.main.temp)
        weather.text = weatherObject.weather[0].description
        tempMin.text = resources.getString(R.string.temperature_min, String.format(Constants.DEGREES_CELSIUS, weatherObject.main.tempMin))
        tempMax.text = resources.getString(R.string.temperature_max, String.format(Constants.DEGREES_CELSIUS, weatherObject.main.tempMax))
        pressure.text = resources.getString(R.string.pressure, weatherObject.main.pressure)
        humidity.text = resources.getString(R.string.humidity, weatherObject.main.humidity)
        windSpeed.text = resources.getString(R.string.wind_speed, weatherObject.wind.speed)
    }

    companion object {
        fun newInstance(): WeatherDetailsFragment {
            return WeatherDetailsFragment()
        }
    }
}
