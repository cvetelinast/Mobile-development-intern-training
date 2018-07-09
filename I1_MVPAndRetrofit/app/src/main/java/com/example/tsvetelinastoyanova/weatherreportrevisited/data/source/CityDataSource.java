package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source;

import android.support.annotation.NonNull;

import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;

public interface CityDataSource {

    interface GetCityCallback {

        void onCityLoaded(CityEntity cityEntity);

        void onCityDoesNotExist();
    }

    void getCity(@NonNull String cityName, @NonNull GetCityCallback callback);

    interface AddCityCallback {

        void onCityAddedSuccessfully(CityEntity cityEntity);

        void onFail();
    }

    interface GetWeatherObjectCallback {

        void onWeatherObjectLoaded(WeatherObject weatherObject);

        void onFail();
    }

}

