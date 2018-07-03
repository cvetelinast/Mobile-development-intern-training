package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import com.example.tsvetelinastoyanova.weatherreportrevisited.BasePresenter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.BaseView;
import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;

import java.util.List;

public interface CitiesListContract {

    interface View extends BaseView<Presenter> {
        void showNewCityAdded(City newCity);

        void showErrorAddingAddedCity();

        void showErrorLoadingCities();

        void showCityDeleted(City deletedCity);

        void showCityLoaded(City city);

        void setWeatherObjectWhenClicked(WeatherObject weatherObject);
    }

    interface Presenter extends BasePresenter {
        void loadCities();

        void addNewCity(String s);

        void updateCity(CityEntity s);

        void deleteCity(City city);

        WeatherObject getWeatherObjectOnIndex(int index);

        void addNewWeatherObject(WeatherObject weatherObject);
    }
}
