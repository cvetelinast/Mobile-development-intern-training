package com.example.tsvetelinastoyanova.weatherreportrevisited.weatherdetails;

import android.util.Log;

public class WeatherDetailsPresenter implements WeatherDetailsContract.Presenter{
    @Override
    public void start() {
        Log.d("tag", "WeatherDetailsPresenter started");
    }
}
