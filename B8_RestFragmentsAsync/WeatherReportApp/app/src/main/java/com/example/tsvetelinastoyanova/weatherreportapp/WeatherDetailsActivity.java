package com.example.tsvetelinastoyanova.weatherreportapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tsvetelinastoyanova.weatherreportapp.fragments.WeatherDetailsFragment;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportapp.util.Utils;

public class WeatherDetailsActivity extends AppCompatActivity {
    WeatherObject weatherObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            WeatherDetailsFragment newFragment = new WeatherDetailsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, newFragment).commit();
            weatherObject = intent.getParcelableExtra(Constants.WEATHER_OBJECTS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (weatherObject != null) {
            WeatherDetailsFragment details = (WeatherDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            details.changeView(weatherObject);
        }
    }
}
