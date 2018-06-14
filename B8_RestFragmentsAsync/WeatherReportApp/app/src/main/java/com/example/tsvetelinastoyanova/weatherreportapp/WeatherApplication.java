package com.example.tsvetelinastoyanova.weatherreportapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class WeatherApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

