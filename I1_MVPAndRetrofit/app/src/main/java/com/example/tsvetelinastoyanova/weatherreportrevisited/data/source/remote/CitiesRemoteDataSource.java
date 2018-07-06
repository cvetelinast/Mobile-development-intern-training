package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.Constants;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CityDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.CityEntityAdapter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitiesRemoteDataSource implements CityDataSource {
    private static CitiesRemoteDataSource INSTANCE;
    List<WeatherObject> weatherObjectList = new ArrayList<>();

    @Override
    public void getCity(@NonNull String cityName, @NonNull GetCityCallback callback) {
        Utils.checkNotNull(cityName);
        Utils.checkNotNull(callback);
        retrofit2.Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService service = retrofit.create(GetDataService.class);
        Call<WeatherObject> call = service.getWeatherForCity(Constants.API_KEY, cityName);
        call.enqueue(new Callback<WeatherObject>() {
            @Override
            public void onResponse(Call<WeatherObject> call, Response<WeatherObject> response) {
                if (response.body() == null) {
                    callback.onCityDoesNotExist();
                } else {
                    synchronized (weatherObjectList) {
                        weatherObjectList.add(response.body());
                    }
                    callback.onCityLoaded(CityEntityAdapter.convertWeatherObjectToCityEntity(response.body()));
                }
            }

            @Override
            public void onFailure(Call<WeatherObject> call, Throwable t) {
                callback.onCityDoesNotExist();
            }
        });
    }

    public static CitiesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CitiesRemoteDataSource();
        }
        return INSTANCE;
    }

    public List<WeatherObject> getWeatherObjectList() {
        return weatherObjectList;
    }

    public void clearWeatherObjects() {
        weatherObjectList.clear();
    }

    public void getWeatherObject(@NonNull String cityName, @NonNull GetWeatherObjectCallback callback) {
        Utils.checkNotNull(cityName);
        Utils.checkNotNull(callback);
        retrofit2.Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService service = retrofit.create(GetDataService.class);
        Call<WeatherObject> call = service.getWeatherForCity(Constants.API_KEY, cityName);
        call.enqueue(new Callback<WeatherObject>() {
            @Override
            public void onResponse(Call<WeatherObject> call, Response<WeatherObject> response) {
                if (response.body() == null) {
                    callback.onFail();
                } else {
                    synchronized (weatherObjectList) {
                        weatherObjectList.add(response.body());
                    }
                    callback.onWeatherObjectLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherObject> call, Throwable t) {
                callback.onFail();
            }
        });
    }
}
