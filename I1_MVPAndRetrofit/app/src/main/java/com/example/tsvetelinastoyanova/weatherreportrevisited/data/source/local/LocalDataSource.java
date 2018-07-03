package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local;

import android.support.annotation.NonNull;

import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CityDataSource;

import java.util.List;

public interface LocalDataSource extends CityDataSource {
    interface LoadCitiesCallback {

        void onCitiesLoaded(List<CityEntity> cities);

        void onDataNotAvailable();

    }

    void getCities(@NonNull LoadCitiesCallback callback);

    void saveCity(@NonNull CityEntity city);

    //  void refreshCity();

    void deleteCity(@NonNull String cityName);
}
