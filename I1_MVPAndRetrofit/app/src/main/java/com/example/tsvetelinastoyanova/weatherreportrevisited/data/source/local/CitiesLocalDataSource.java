package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CityDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.AppExecutors;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;

import java.util.List;

public class CitiesLocalDataSource implements CityDataSource, LocalDataSource {
    private static volatile CitiesLocalDataSource INSTANCE;

    private CityDao cityDao;

    private AppExecutors mAppExecutors;

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
        Utils.checkNotNull(callback);
        Runnable runnable = () -> {
            final List<CityEntity> cities = cityDao.getAll();
            mAppExecutors.mainThread().execute(() -> {
                if (cities.isEmpty()) {
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
        Utils.checkNotNull(cityName);
        Utils.checkNotNull(callback);
        Runnable runnable = () -> {
            final CityEntity city = cityDao.getCity(cityName);
            mAppExecutors.mainThread().execute(() -> {
                if (city == null) {
                    // This will be called if the table is new or just empty.
                    callback.onCityDoesNotExist();
                } else {
                    callback.onCityLoaded(city);
                }
            });
        };

        mAppExecutors.databaseIO().execute(runnable);
    }

    @Override
    public void addCity(@NonNull CityEntity cityEntity, LocalDataSource.AddCityCallback addCityCallback) {
        Utils.checkNotNull(cityEntity);
        Utils.checkNotNull(addCityCallback);
        Runnable runnable = () -> {
            if (cityEntity != null) {
                cityDao.insertCity(cityEntity);
            }
            mAppExecutors.mainThread().execute(() -> {
                addCityCallback.onCityAddedSuccessfully(cityEntity);
            });
        };
        mAppExecutors.databaseIO().execute(runnable);
    }

    @Override
    public void deleteCity(@NonNull String cityName) {

    }
}
