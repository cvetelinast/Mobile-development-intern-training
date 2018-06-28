package com.example.tsvetelinastoyanova.weatherreportrevisited.network;

import com.example.tsvetelinastoyanova.weatherreportrevisited.model.Example;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("weather")
    Call<Example> getWeatherForCity(@Query("APPID") String appId, @Query("q") String city);
}