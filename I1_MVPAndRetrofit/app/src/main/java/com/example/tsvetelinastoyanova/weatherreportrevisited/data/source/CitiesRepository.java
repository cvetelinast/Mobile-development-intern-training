package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

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


        //  cacheIsDirty = true;   // todo: where to make cache dirty
        Utils.checkNotNull(getCityCallback);
        if (!cacheIsDirty && !weatherObjectsCache.isEmpty()) {
            Log.d("tag", "Load from cache weather objects " + weatherObjectsCache.size());
            synchronized (weatherObjectsCache) {
                for (WeatherObject weatherObject : weatherObjectsCache) {
                    Log.d("tag", "loading city " + weatherObject.getName());
                    CityEntity cityEntity = CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject);
                    getCityCallback.onCityLoaded(cityEntity);
                }
            }
        } else {
            localDataSource.getCities(new LocalDataSource.LoadCitiesCallback() {
                @Override
                public void onCitiesLoaded(List<CityEntity> cities) {
                    // todo tyk
                    if (!cities.isEmpty()) {
                        Log.d("tag", "cities not empty, length " + cities.size());
                        for (CityEntity cityEntity : cities) {

                            remoteDataSource.getWeatherObject(cityEntity.getName(), new GetWeatherObjectCallback() {
                                @Override
                                public void onWeatherObjectLoaded(WeatherObject weatherObject) {
                                    synchronized (weatherObjectsCache) {
                                        weatherObjectsCache.add(weatherObject);
                                    }
                                    getCityCallback.onCityLoaded(CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject));
                                }

                                @Override
                                public void onFail() {
                                    getCityCallback.onCityDoesNotExist();
                                }
                            });

                           /* remoteDataSource.getCity(cityEntity.getName(), new GetCityCallback() {
                                @Override
                                public void onCityLoaded(CityEntity city) {
                                    Log.d("tag", "loading city " + city.getName());
                                    getCityCallback.onCityLoaded(city);
                                    updateCache();
                                }

                                @Override
                                public void onCityDoesNotExist() {
                                    getCityCallback.onCityDoesNotExist();
                                }
                            });*/
                        }
                    } else {
                        Log.d("tag", "cities empty");
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
        synchronized (weatherObjectsCache) {
            weatherObjectsCache.addAll(remoteDataSource.getWeatherObjectList());
            remoteDataSource.clearWeatherObjects();
        }
        //     cacheIsDirty = false;
    }

    public void refreshWeatherObjectInCache(String cityName) {
        synchronized (weatherObjectsCache) {
            for (WeatherObject newObject : remoteDataSource.getWeatherObjectList()) {
                if (newObject.getName().equals(cityName)) {
                    for (int i = 0; i < weatherObjectsCache.size(); i++) {
                        if (newObject.getName().equals(cityName)) {
                            weatherObjectsCache.set(i, newObject);
                            return;
                        }
                    }
                }
            }
        }
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
                    public void onFail() {
                        callback.onFail();
                    }
                });


                /*remoteDataSource.getCity(cityName, new GetCityCallback() {
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
                });*/
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
            for (WeatherObject weatherObject : weatherObjectsCache) {
                if (weatherObject.getName().equals(cityName)) {
                    return weatherObject;
                }
            }
            getWeatherObject(cityName);
            for (WeatherObject weatherObject : weatherObjectsCache) {
                if (weatherObject.getName().equals(cityName)) {
                    return weatherObject;
                }
            }
        }
        return null; // todo: kakvo pravim ako ne sushtestvyva takuv grad
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
                            synchronized (weatherObjectsCache) {
                                weatherObjectsCache.add(weatherObject);
                            }
                            CityEntity newCity = CityEntityAdapter.convertWeatherObjectToCityEntity(weatherObject);
                            localDataSource.refreshCity(newCity, (() -> {
                                getCityCallback.onCityLoaded(newCity);
                            }));
                        }

                        @Override
                        public void onFail() {
                            getCityCallback.onCityDoesNotExist();
                        }
                    });
                }


                   /* remoteDataSource.getCity(oldCity.getName(), new GetCityCallback() {
                        @Override
                        public void onCityLoaded(CityEntity newCity) {
                            refreshWeatherObjectInCache(newCity.getName());
                            localDataSource.refreshCity(newCity, (() -> {
                                getCityCallback.onCityLoaded(newCity);
                            }));
                        }

                        @Override
                        public void onCityDoesNotExist() {
                            getCityCallback.onCityDoesNotExist();
                        }
                    });
                }*/

                @Override
                public void onCityDoesNotExist() {
                    getCityCallback.onCityDoesNotExist();
                }
            });
                   /*
                    refreshWeatherObjectsInCache();
                    localDataSource.refreshCity(cityEntity, new LocalDataSource.RefreshCityCallback() {
                        @Override
                        public void onRefreshCitySuccessfully() {
                            getCityCallback.onCityLoaded(cityEntity);
                        }
                    });*/
                    /*refreshCity(cityEntity, new AddCityCallback() {
                        @Override
                        public void onCityAddedSuccessfully(CityEntity cityEntity) {
                            getCityCallback.onCityLoaded(cityEntity);
                        }

                        @Override
                        public void onFail() {
                            getCityCallback.onCityDoesNotExist();
                        }
                    });*/
        }

               /* @Override
                public void onCityDoesNotExist() {
                    getCityCallback.onCityDoesNotExist();
                }
            });*/
    }
    // getCities(getCityCallback);
/*
// todo: dolniqt blok e ako resha da ne gi triq vednaga, no togava kato vlqza sled nqkolko sekyndi v prilojenieto, pak shte se pokajat iztritite ot predi malko
        for (City c : cities) {
            remoteDataSource.getCity(c.getName(), new GetCityCallback() {
                @Override
                public void onCityLoaded(CityEntity cityEntity) {
                    getCityCallback.onCityLoaded(cityEntity);
                }

                @Override
                public void onCityDoesNotExist() {
                    getCityCallback.onCityDoesNotExist();
                }
            });
        }*/


   /* private void refreshCity(CityEntity city, @NonNull AddCityCallback callback) {
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
    }*/

    private void deleteWeatherObjectFromCache(String cityName) {
        for (WeatherObject weatherObject : weatherObjectsCache) {
            if (weatherObject.getName().equals(cityName)) {
                weatherObjectsCache.remove(weatherObject);
                return;
            }
        }
    }

    private void getWeatherObject(String cityName) {
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
