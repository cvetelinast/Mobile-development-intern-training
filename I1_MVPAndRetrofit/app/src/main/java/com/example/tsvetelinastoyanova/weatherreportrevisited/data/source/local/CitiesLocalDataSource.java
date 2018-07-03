package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local;

import android.support.annotation.NonNull;

import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CityDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.AppExecutors;

import java.util.List;

public class CitiesLocalDataSource implements CityDataSource, LocalDataSource {
    private static volatile CitiesLocalDataSource INSTANCE;

    private CityDao cityDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private CitiesLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull CityDao cityDao) {
        mAppExecutors = appExecutors;
        this.cityDao = cityDao;
    }

    public static CitiesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull CityDao cityDao) {
        if (INSTANCE == null) {
            synchronized (CitiesLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CitiesLocalDataSource(appExecutors, cityDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getCities(@NonNull LocalDataSource.LoadCitiesCallback callback) {
        Runnable runnable = () -> {
            final List<CityEntity> cities = cityDao.getAll();
            mAppExecutors.mainThread().execute(() -> {
                if (cities.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable();
                } else {
                    callback.onCitiesLoaded(cities);
                }
            });
        };

        mAppExecutors.databaseIO().execute(runnable);
    }

    @Override
    public void getCity(@NonNull String cityName, @NonNull GetCityCallback callback) {

    }

    @Override
    public void saveCity(@NonNull CityEntity city) {

    }

    @Override
    public void deleteCity(@NonNull String cityName) {

    }
}
