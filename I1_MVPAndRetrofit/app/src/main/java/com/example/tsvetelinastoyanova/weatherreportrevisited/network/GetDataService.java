package com.example.tsvetelinastoyanova.weatherreportrevisited.network;

import com.example.tsvetelinastoyanova.weatherreportrevisited.model.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("weather?q={city}&APPID=956b0cec24dc9a86dddab6ed85a14dd5")
    Call<Weather> getWeatherForCity(@Path("city") String city);
}