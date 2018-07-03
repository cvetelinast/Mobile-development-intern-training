package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source;

import android.support.annotation.NonNull;

import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;

import java.util.List;

public interface CityDataSource {

    interface GetCityCallback {

        void onCityLoaded(CityEntity city);

        void onDataNotAvailable();
    }


    void getCity(@NonNull String cityName, @NonNull GetCityCallback callback);
}

