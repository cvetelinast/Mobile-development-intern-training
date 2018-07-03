package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CityDataSource;

public class CitiesRemoteDataSource implements CityDataSource {
    private static CitiesRemoteDataSource INSTANCE;
    @Override
    public void getCity(@NonNull String cityName, @NonNull GetCityCallback callback) {
        Log.d("tag", "get remote city " + cityName);
    }

    public static CitiesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CitiesRemoteDataSource();
        }
        return INSTANCE;
    }


}
