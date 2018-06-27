package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import com.example.tsvetelinastoyanova.weatherreportrevisited.BasePresenter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.BaseView;
import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization.CityRowView;

import java.util.List;

public interface CitiesListContract {

    interface View extends BaseView<Presenter> {
        void showNewCityAdded(City newCity);
        void showCityDeleted(City deletedCity);
    }

    interface Presenter extends BasePresenter {
        List<City> loadCities();
        void addNewCity(String s);
   //     void deleteCity(City city);
    }
}
