package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote;

import android.support.annotation.NonNull;

import com.example.tsvetelinastoyanova.weatherreportrevisited.Constants;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CityDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitiesRemoteDataSource {
    private static CitiesRemoteDataSource INSTANCE;

    public static CitiesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CitiesRemoteDataSource();
        }
        return INSTANCE;
    }

    public void getWeatherObject(@NonNull String cityName, @NonNull CityDataSource.GetWeatherObjectCallback callback) {
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
