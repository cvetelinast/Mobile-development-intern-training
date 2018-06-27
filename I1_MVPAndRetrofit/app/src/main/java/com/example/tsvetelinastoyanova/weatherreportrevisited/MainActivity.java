package com.example.tsvetelinastoyanova.weatherreportrevisited;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.CitiesListFragment;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.CitiesListPresenter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.weatherdetails.WeatherDetailsFragment;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.ActivityUtils;
import com.example.tsvetelinastoyanova.weatherreportrevisited.weatherdetails.WeatherDetailsPresenter;

public class MainActivity extends AppCompatActivity {

    private CitiesListPresenter citiesListPresenter;
    private WeatherDetailsPresenter weatherDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.list_fragment_container) != null) {
            CitiesListFragment citiesListFragment =
                    (CitiesListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment_container);
            if (citiesListFragment == null) {
                // Create the fragment
                citiesListFragment = CitiesListFragment.newInstance();
                citiesListPresenter = new CitiesListPresenter(citiesListFragment);
                ActivityUtils.addFragmentToActivity(
                        getSupportFragmentManager(), citiesListFragment, R.id.list_fragment_container);
            }
        }
        if (findViewById(R.id.details_fragment_container) != null) {
            WeatherDetailsFragment weatherDetailsFragment =
                    (WeatherDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment_container);
            if (weatherDetailsFragment == null) {
                // Create the fragment
                weatherDetailsFragment = WeatherDetailsFragment.newInstance();
                // todo: make presenter
                ActivityUtils.addFragmentToActivity(
                        getSupportFragmentManager(), weatherDetailsFragment, R.id.details_fragment_container);
            }
        }
    }
}
