package com.example.tsvetelinastoyanova.registrationloginapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class LoginApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

