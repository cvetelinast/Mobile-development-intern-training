package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CitiesRepository;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CityDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local.LocalDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.CityEntityAdapter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;

import java.util.List;

public class CitiesListPresenter implements CitiesListContract.Presenter {
    private final CitiesListContract.View view;
    private final CitiesRepository citiesRepository;
    private Handler handler = new Handler();
    private Runnable refresh;

    public CitiesListPresenter(CitiesListContract.View view, @NonNull CitiesRepository citiesRepository) {
        this.view = view;
        view.setPresenter(this);
        this.citiesRepository = Utils.checkNotNull(citiesRepository);
    }

    @Override
    public void start() {
        loadCities();
        setTimerToRefresh();
    }

    @Override
    public void loadCities() {
        citiesRepository.getCities(new CityDataSource.GetCityCallback() {
            @Override
            public void onCityLoaded(CityEntity cityEntity) {
                view.showCityLoaded(CityEntityAdapter.convertCityEntityToCity(cityEntity));
            }

            @Override
            public void onCityDoesNotExist() {
                view.showErrorLoadingCities();
            }
        });
    }

    @Override
    public void addNewCity(String cityName) {
        citiesRepository.addCity(cityName, new CityDataSource.AddCityCallback() {
            @Override
            public void onCityAddedSuccessfully(CityEntity cityEntity) {
                view.showNewCityAdded(CityEntityAdapter.convertCityEntityToCity(cityEntity));
            }

            @Override
            public void onFail() {
                view.showErrorAddingAddedCity();
            }
        });
    }

    @Override
    public void deleteCity(City city) {
        citiesRepository.deleteCity(city.getName(), new LocalDataSource.DeleteCityCallback() {
            @Override
            public void onCityDeletedSuccessfully() {
                view.showCityDeleted();
            }

            @Override
            public void onFail() {
                view.showErrorDeleteCity();
            }
        });
    }


    @Override
    public WeatherObject getWeatherObjectOnClick(String cityName) {
        return citiesRepository.getWeatherObjectWithName(cityName);
    }

    @Override
    public void refreshCities(List<City> cities) {
        if (!cities.isEmpty()) {
            citiesRepository.refreshCities(cities, new CityDataSource.GetCityCallback() {
                @Override
                public void onCityLoaded(CityEntity cityEntity) {
                    view.showCityUpdated(CityEntityAdapter.convertCityEntityToCity(cityEntity));
                }

                @Override
                public void onCityDoesNotExist() {
                    view.showErrorLoadingCities();
                }
            });
        }
    }

    private void setTimerToRefresh() {
        refresh = () -> {
            refreshCities(view.getDisplayedCities());
            //handler.postDelayed(refresh, 10000);
            handler.postDelayed(refresh, 900000);
        };
        handler.post(refresh);
    }
}
