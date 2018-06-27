package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;

import java.util.ArrayList;
import java.util.List;

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
    }
}
