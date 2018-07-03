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

    interface AddCityCallback{

        void onCityAddedSuccessfully(CityEntity cityEntity);

        void onFail();
    }

    void getCities(@NonNull LoadCitiesCallback callback);

    void addCity(@NonNull CityEntity cityEntity, AddCityCallback addCityCallback);

    //  void refreshCity();

    void deleteCity(@NonNull String cityName);
}
