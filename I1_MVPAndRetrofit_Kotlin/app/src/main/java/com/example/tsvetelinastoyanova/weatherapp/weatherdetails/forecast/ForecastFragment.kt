package com.example.tsvetelinastoyanova.weatherapp.weatherdetails.forecast

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.tsvetelinastoyanova.weatherapp.R

class ForecastFragment : Fragment(), ForecastContract.View {
    private var presenter: ForecastContract.Presenter? = null
    private var city: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast_child, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun showErrorLoadingForecast() {
        Toast.makeText(context, R.string.problem_loading_forecast, Toast.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ForecastFragment()
    }

    override fun setPresenter(presenter: ForecastContract.Presenter) {
        this.presenter = presenter
    }

    fun setCityName(cityName: String) {
        city = cityName
        presenter?.startGettingForecastForCity(cityName)
    }
}
