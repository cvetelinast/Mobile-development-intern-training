package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local.LocalDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote.CitiesRemoteDataSource;
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

    List<WeatherObject> weatherObjectsCache = new ArrayList<>();

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

//    public List<WeatherObject> getWeatherObjectsCache() {
//        return weatherObjectsCache;
//    }

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
        Utils.checkNotNull(getCityCallback);
        if (!weatherObjectsCache.isEmpty()) {
            getCitiesFromCache(getCityCallback);
        } else {
            getCitiesFromDataSource(getCityCallback);
        }
    }

    private void getCitiesFromDataSource(@NonNull GetCityCallback getCityCallback) {
        localDataSource.getCities(new LocalDataSource.LoadCitiesCallback() {
            @Override
            public void onCitiesLoaded(List<CityEntity> cities) {
                if (!cities.isEmpty()) {
                    for (CityEntity cityEntity : cities) {
                        remoteDataSource.getWeatherObject(cityEntity.getName(), new GetWeatherObjectCallback() {
                            @Override
                            public void onWeatherObjectLoaded(WeatherObject weatherObject) {
                                refreshWeatherObjectInCache(weatherObject);
                                getCityCallback.onCityLoaded(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject));
                            }

                            @Override
                            public void onFail() {
                                getCityCallback.onCityDoesNotExist();
                            }
                        });
                    }
                }
            }

            @Override
            public void onDataNotAvailable() {
                getCityCallback.onCityDoesNotExist();
            }
        });
    }

    private void getCitiesFromCache(@NonNull GetCityCallback getCityCallback) {
        synchronized (weatherObjectsCache) {
            for (WeatherObject weatherObject : weatherObjectsCache) {
                CityEntity cityEntity = CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject);
                getCityCallback.onCityLoaded(cityEntity);
            }
        }
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
                callback.onCityDoesNotExist();
            }
        });
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
                remoteDataSource.getWeatherObject(cityName, new GetWeatherObjectCallback() {
                    @Override
                    public void onWeatherObjectLoaded(WeatherObject weatherObject) {
                        localDataSource.addCity(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject), new LocalDataSource.AddCityCallback() {
                            @Override
                            public void onCityAddedSuccessfully(CityEntity cityEntity) {
                                refreshWeatherObjectInCache(weatherObject);
                                callback.onCityAddedSuccessfully(cityEntity);
                            }

                            @Override
                            public void onFail() {
                                callback.onFail();
                            }
                        });
                    }

                    @Override
                    public void onFail() {
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

    public void deleteCity(@NonNull String cityName, LocalDataSource.DeleteCityCallback deleteCityCallback) {
        Utils.checkNotNull(cityName);
        localDataSource.deleteCity(cityName, new LocalDataSource.DeleteCityCallback() {
            @Override
            public void onCityDeletedSuccessfully() {
                deleteWeatherObjectFromCache(cityName);
                deleteCityCallback.onCityDeletedSuccessfully();
            }

            @Override
            public void onFail() {
                deleteCityCallback.onFail();
            }
        });
    }

    public WeatherObject getWeatherObjectWithName(String cityName) {
        synchronized (weatherObjectsCache) {
            int saveIndex;
            for (WeatherObject weatherObject: weatherObjectsCache) {
                if (weatherObject.getName().equals(cityName)) {
                    return weatherObject;
                }
            }
            saveIndex = weatherObjectsCache.size();

            addWeatherObjectToCacheFromRemote(cityName);

            if (saveIndex > weatherObjectsCache.size()) {
                for (int i = saveIndex - 1; i < weatherObjectsCache.size(); i++) {
                    if (weatherObjectsCache.get(i).getName().equals(cityName)) {
                        return weatherObjectsCache.get(i);
                    }
                }
            }
        }
        return null;
    }

    public void refreshCities(List<City> cities, @NonNull CityDataSource.GetCityCallback getCityCallback) {
        /* foreach city
            load from DB
            get name and load from API
            refresh cache
            refresh DB
            refresh UI
         */
        for (City c : cities) {
            getCity(c.getName(), new GetCityCallback() {
                @Override
                public void onCityLoaded(CityEntity oldCity) {
                    remoteDataSource.getWeatherObject(oldCity.getName(), new GetWeatherObjectCallback() {
                        @Override
                        public void onWeatherObjectLoaded(WeatherObject weatherObject) {
                            refreshWeatherObjectInCache(weatherObject);
                            CityEntity newCity = CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject);
                            localDataSource.refreshCity(newCity, (() -> getCityCallback.onCityLoaded(newCity)));
                        }

                        @Override
                        public void onFail() {
                            getCityCallback.onCityDoesNotExist();
                        }
                    });
                }

                @Override
                public void onCityDoesNotExist() {
                    getCityCallback.onCityDoesNotExist();
                }
            });
        }
    }

    public void refreshWeatherObjectInCache(WeatherObject newObject) {
        synchronized (this) {
            for (int i = 0; i < weatherObjectsCache.size(); i++) {
                if (weatherObjectsCache.get(i).getName().equals(newObject.getName())) {
                    weatherObjectsCache.set(i, newObject);
                    return;
                }
            }

            weatherObjectsCache.add(newObject);
        }
    }

    private void deleteWeatherObjectFromCache(String cityName) {
        synchronized (weatherObjectsCache) {
            for (WeatherObject weatherObject : weatherObjectsCache) {
                if (weatherObject.getName().equals(cityName)) {
                    weatherObjectsCache.remove(weatherObject);
                    return;
                }
            }
        }
    }

    private void addWeatherObjectToCacheFromRemote(String cityName) {
        synchronized (weatherObjectsCache) {
            remoteDataSource.getWeatherObject(cityName, new GetWeatherObjectCallback() {
                @Override
                public void onWeatherObjectLoaded(WeatherObject weatherObject) {
                    weatherObjectsCache.add(weatherObject);
                }

                @Override
                public void onFail() {

                }
            });
        }
    }
}
