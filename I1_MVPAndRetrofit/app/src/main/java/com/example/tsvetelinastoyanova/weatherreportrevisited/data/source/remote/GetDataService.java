package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote;

import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("weather")
    Call<WeatherObject> getWeatherForCity(@Query("APPID") String appId, @Query("q") String city);
}