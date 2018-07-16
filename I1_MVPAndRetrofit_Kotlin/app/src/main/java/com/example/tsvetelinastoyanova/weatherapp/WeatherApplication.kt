package com.example.tsvetelinastoyanova.weatherapp

import android.app.Application
import com.facebook.stetho.Stetho

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}
