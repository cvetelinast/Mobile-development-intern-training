package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local.LocalDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote.CitiesRemoteDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.Weather;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.CityEntityAdapter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CitiesRepository implements CityDataSource {
    private static CitiesRepository INSTANCE = null;
    private final LocalDataSource localDataSource;
    private final CitiesRemoteDataSource remoteDataSource;

    private final List<WeatherObject> weatherObjectsCache = new ArrayList<>();

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

    public void getCities(@NonNull CityDataSource.GetCityCallback getCityCallback) {
        /*
        if dirty cache:
        1) getAll cities from DB
                1.1) success - forEach cityEntity request to the API
                    1.1.1) success - return list of cityEntities
                    1.1.2) fail - error with internet
                1.2) fail - error
         else:
         2) getAll cities from DB
                2.1) success - return cityEntities from DB one by one
                2.2) fail - error
         */


        //  cacheIsDirty = true;
        Utils.checkNotNull(getCityCallback);
        if (!cacheIsDirty && !weatherObjectsCache.isEmpty()) {
            for (WeatherObject weatherObject : weatherObjectsCache) {
                CityEntity cityEntity = CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject);
                getCityCallback.onCityLoaded(cityEntity);
            }
        } else {
            localDataSource.getCities(new LocalDataSource.LoadCitiesCallback() {
                @Override
                public void onCitiesLoaded(List<CityEntity> cities) {
                    for (CityEntity cityEntity : cities) {
                        remoteDataSource.getCity(cityEntity.getName(), new GetCityCallback() {
                            @Override
                            public void onCityLoaded(CityEntity city) {
                                getCityCallback.onCityLoaded(city);
                                updateCache();
                            }

                            @Override
                            public void onCityDoesNotExist() {
                                getCityCallback.onCityDoesNotExist();
                            }
                        });
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    getCityCallback.onCityDoesNotExist();
                }
            });
        } /*else {
            localDataSource.getCities(new LocalDataSource.LoadCitiesCallback() {
                @Override
                public void onCitiesLoaded(List<CityEntity> cities) {
                    for (CityEntity cityEntity : cities) {
                        getCityCallback.onCityLoaded(cityEntity);
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    getCityCallback.onCityDoesNotExist();
                }
            });
        }*/
    }

    public void saveCity(@NonNull CityEntity city) {

    }

    @Override
    public void getCity(@NonNull String cityName, @NonNull GetCityCallback callback) {
        Utils.checkNotNull(cityName);
        Utils.checkNotNull(callback);

        localDataSource.getCity(cityName, new GetCityCallback() {
            @Override
            public void onCityLoaded(CityEntity city) {
                callback.onCityLoaded(city);
            }

            @Override
            public void onCityDoesNotExist() {
                Log.d("Tag", "on city not available");
            }
        });
    }

    public void updateCache() {
        weatherObjectsCache.addAll(remoteDataSource.getWeatherObjectList());
        remoteDataSource.clearWeatherObjects();
    }

    public void addCity(String cityName, @NonNull AddCityCallback callback) {
        /* 1) check if city exists in database
            1.1) exists - onCityAlreadyExists()
            1.2) does not exist - api request()
                1.2.1) success
                    1.2.1.1) write to DB
                        1.2.1.1.1) success - return message
                        1.2.1.1.2) fail - return message
                1.2.2) fail - return message */

        Utils.checkNotNull(cityName);
        Utils.checkNotNull(callback);

        localDataSource.getCity(cityName, new GetCityCallback() {
            @Override
            public void onCityDoesNotExist() {
                remoteDataSource.getCity(cityName, new GetCityCallback() {
                    @Override
                    public void onCityLoaded(CityEntity city) {
                        localDataSource.addCity(city, new LocalDataSource.AddCityCallback() {
                            @Override
                            public void onCityAddedSuccessfully(CityEntity cityEntity) {
                                callback.onCityAddedSuccessfully(cityEntity);
                                updateCache();
                            }

                            @Override
                            public void onFail() {
                                callback.onFail();
                            }
                        });
                    }

                    @Override
                    public void onCityDoesNotExist() {
                        callback.onFail();
                    }
                });
            }

            @Override
            public void onCityLoaded(CityEntity city) {
                callback.onFail();
            }
        });
    }

    public void deleteCity(@NonNull String cityName) {
        Utils.checkNotNull(cityName);
        localDataSource.deleteCity(cityName);
    }
}
