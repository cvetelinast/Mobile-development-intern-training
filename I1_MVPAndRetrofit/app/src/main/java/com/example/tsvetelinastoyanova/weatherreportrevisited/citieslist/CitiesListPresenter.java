package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import android.content.Context;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.Constants;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.Example;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.Weather;
import com.example.tsvetelinastoyanova.weatherreportrevisited.network.GetDataService;
import com.example.tsvetelinastoyanova.weatherreportrevisited.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CitiesListPresenter implements CitiesListContract.Presenter {
    private final List<City> cities = new ArrayList<>();

    private final CitiesListContract.View fragment;

    public CitiesListPresenter(CitiesListContract.View fragment) {
        this.fragment = fragment;
        fragment.setPresenter(this);
    }

    @Override
    public void start() {
        Log.d("tag", "CitiesListPresenter started");
    }

    @Override
    public List<City> loadCities() {
        cities.add(new City("Monaco", 15, 0x7f070064));
        cities.add(new City("Sofia", 15, 0x7f070064));
        return cities;
    }

    @Override
    public void addNewCity(String cityName) {

        Log.d("tag", "Add new city " + cityName);
        retrofit2.Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService service = retrofit.create(GetDataService.class);
        Call<Example> call = service.getWeatherForCity(Constants.API_KEY, cityName);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                //  progressDoalog.dismiss();
                receiveWeatherForCity(response.body());
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                //   progressDoalog.dismiss();
                Log.e("failed", t.getLocalizedMessage());
                //   Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*Create handle for the RetrofitInstance interface*/


    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void receiveWeatherForCity(Example weather) {
        Log.d("tag", "generate data list with weather " + weather.getWeather().get(0).getMain());
       /* recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(this,photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);*/
    }
}
