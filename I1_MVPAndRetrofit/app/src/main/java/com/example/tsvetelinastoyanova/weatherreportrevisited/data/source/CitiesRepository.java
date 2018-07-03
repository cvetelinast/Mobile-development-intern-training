package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local.LocalDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote.CitiesRemoteDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CitiesRepository implements CityDataSource {
    private static CitiesRepository INSTANCE = null;
    private final LocalDataSource localDataSource;
    private final CitiesRemoteDataSource remoteDataSource;

    private boolean cacheIsDirty = false;

    private CitiesRepository(@NonNull LocalDataSource localDataSource,
                             @NonNull CitiesRemoteDataSource remoteDataSource) {
        this.localDataSource = checkNotNull(localDataSource);
        this.remoteDataSource = checkNotNull(remoteDataSource);
    }

    public static CitiesRepository getInstance(LocalDataSource localDataSource,
                                               CitiesRemoteDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CitiesRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void getCities(@NonNull CityDataSource.GetCityCallback cityCallback) {
        Log.d("tag", "getCities() in CitiesRepository");
        checkNotNull(cityCallback);
        if (cacheIsDirty) {
            localDataSource.getCities(new LocalDataSource.LoadCitiesCallback() {
                @Override
                public void onCitiesLoaded(List<CityEntity> cities) {
                    for (CityEntity cityEntity : cities) {
                        remoteDataSource.getCity(cityEntity.getName(), new GetCityCallback() {
                            @Override
                            public void onCityLoaded(CityEntity city) {
                                // localDataSource.updateCity(city);
                                cityCallback.onCityLoaded(city);
                                // todo: only this method is common
                            }

                            @Override
                            public void onDataNotAvailable() {
                                cityCallback.onDataNotAvailable();
                            }
                        });
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    Log.d("Tag", "on data not available");
                }
            });
        } else {
            localDataSource.getCities(new LocalDataSource.LoadCitiesCallback() {
                @Override
                public void onCitiesLoaded(List<CityEntity> cities) {
                    Log.d("tag", "length of cities is " + cities.size());
                    for (CityEntity cityEntity : cities) {
                        cityCallback.onCityLoaded(cityEntity);
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    Log.d("tag", "on data not available");
                }
            });
        }
    }

    public void saveCity(@NonNull CityEntity city) {

    }

    @Override
    public void getCity(@NonNull String cityName, @NonNull GetCityCallback callback) {
        checkNotNull(cityName);
        checkNotNull(callback);

        localDataSource.getCity(cityName, new GetCityCallback() {
            @Override
            public void onCityLoaded(CityEntity city) {
                callback.onCityLoaded(city);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d("Tag", "on city not available");
            }
        });
    }

    /*@Override
    public void refreshCity() {

    }*/

    public void deleteCity(@NonNull String cityName) {
        checkNotNull(cityName);
        localDataSource.deleteCity(cityName);
    }
}
