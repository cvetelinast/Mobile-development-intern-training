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

    interface DeleteCityCallback{

        void onCityDeletedSuccessfully();

        void onFail();
    }

    interface RefreshCityCallback{

        void onRefreshCitySuccessfully();

    }

    void getCities(@NonNull LoadCitiesCallback callback);

    void addCity(@NonNull CityEntity cityEntity, AddCityCallback addCityCallback);

    void refreshCity(@NonNull CityEntity cityEntity, LocalDataSource.RefreshCityCallback refreshCityCallback);

    void deleteCity(@NonNull String cityName, LocalDataSource.DeleteCityCallback callback);
}
