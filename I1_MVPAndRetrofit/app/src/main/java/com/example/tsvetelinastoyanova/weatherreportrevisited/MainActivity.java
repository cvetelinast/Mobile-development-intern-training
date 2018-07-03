package com.example.tsvetelinastoyanova.weatherreportrevisited;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.CitiesListFragment;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.CitiesListPresenter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;
import com.example.tsvetelinastoyanova.weatherreportrevisited.weatherdetails.WeatherDetailsFragment;
import com.example.tsvetelinastoyanova.weatherreportrevisited.weatherdetails.WeatherDetailsPresenter;

public class MainActivity extends AppCompatActivity implements CitiesListFragment.OnClickCityDelegate {

    private WeatherDetailsFragment weatherDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.list_fragment_container) != null) {
            createCitiesListFragment();
        }
        if (findViewById(R.id.details_fragment_container) != null) {
            createWeatherListFragment();
        }
    }

    @Override
    public void onClickCity(WeatherObject weatherObject) {
        if (Utils.isTablet(this)) {
            weatherDetailsFragment.changeView(weatherObject);
        } else {
            Intent i = new Intent(MainActivity.this, WeatherDetailsActivity.class);
            i.putExtra(Constants.WEATHER_OBJECTS, weatherObject);
            startActivity(i);
        }
    }

    private void createCitiesListFragment() {
        CitiesListFragment citiesListFragment =
                (CitiesListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment_container);
        if (citiesListFragment == null) {
            // Create the fragment
            citiesListFragment = CitiesListFragment.newInstance();
            CitiesListPresenter citiesListPresenter = new CitiesListPresenter(citiesListFragment, this.getApplicationContext(), Utils.provideCityRepository(getApplicationContext()));
            citiesListFragment.setPresenter(citiesListPresenter);
            Utils.addFragmentToActivity(
                    getSupportFragmentManager(), citiesListFragment, R.id.list_fragment_container);
        }
    }

    private void createWeatherListFragment() {
        weatherDetailsFragment =
                (WeatherDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment_container);
        if (weatherDetailsFragment == null) {
            // Create the fragment
            weatherDetailsFragment = WeatherDetailsFragment.newInstance();
            WeatherDetailsPresenter weatherDetailsPresenter = new WeatherDetailsPresenter();
            weatherDetailsFragment.setPresenter(weatherDetailsPresenter);
            Utils.addFragmentToActivity(
                    getSupportFragmentManager(), weatherDetailsFragment, R.id.details_fragment_container);
        }
    }

}
