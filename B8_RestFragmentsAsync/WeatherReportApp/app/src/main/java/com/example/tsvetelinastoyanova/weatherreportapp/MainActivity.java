package com.example.tsvetelinastoyanova.weatherreportapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tsvetelinastoyanova.weatherreportapp.fragments.CitiesListFragment;
import com.example.tsvetelinastoyanova.weatherreportapp.fragments.WeatherDetailsFragment;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportapp.util.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CitiesListFragment.OnHeadlineSelectedListener {
    List<WeatherObject> weatherObjects;
    WeatherDetailsFragment weatherDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.list_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            CitiesListFragment firstFragment = new CitiesListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, firstFragment).commit();
        }
        if (findViewById(R.id.details_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            weatherDetailsFragment = new WeatherDetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_fragment_container, weatherDetailsFragment).commit();
        }

    }

    @Override
    public void onWeatherObjectsLoaded(List<WeatherObject> weatherObjects) {
        this.weatherObjects = weatherObjects;
        if (Utils.isTablet(this)) {
           // weatherDetailsFragment.changeView(this.weatherObjects.get(0));
            // problem with this
        }

    }

    @Override
    public void onWeatherObjectClicked(int position) {
        if (Utils.isTablet(this)) {
            weatherDetailsFragment.changeView(weatherObjects.get(position));
        } else {
            Intent i = new Intent(MainActivity.this, WeatherDetailsActivity.class);
            i.putExtra(Constants.WEATHER_OBJECTS, weatherObjects.get(position));
            startActivity(i);
        }
    }
}
