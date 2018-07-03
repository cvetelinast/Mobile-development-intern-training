package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.Constants;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CitiesRepository;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CityDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local.AppDatabase;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote.GetDataService;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote.RetrofitClientInstance;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.CityEntityAdapter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.ImageOperator;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitiesListPresenter implements CitiesListContract.Presenter {
    private final List<City> cities = new ArrayList<>();
    private WeakReference<Context> contextRef;
    private final List<WeatherObject> weatherObjects = new ArrayList<>();
    private final CitiesListContract.View fragment;
    private final ExecutorService notMainThread = Executors.newSingleThreadExecutor();
    private final CitiesRepository citiesRepository;

    public CitiesListPresenter(CitiesListContract.View fragment, Context context, @NonNull CitiesRepository citiesRepository) {
        this.fragment = fragment;
        fragment.setPresenter(this);
        contextRef = new WeakReference<>(context);
        this.citiesRepository = Utils.checkNotNull(citiesRepository);
    }

    @Override
    public void start() {
        loadCities();
        Log.d("tag", "CitiesListPresenter started");
    }

    @Override
    public /*List<City> */ void loadCities() {
      //  notMainThread.execute(() -> {
        citiesRepository.getCities(new CityDataSource.GetCityCallback() {
            @Override
            public void onCityLoaded(CityEntity city) {
                Log.d("tag", "received city " + city.getName());
            }

            @Override
            public void onDataNotAvailable() {
                    Log.d("tag", "Not available data");
            }
        });
           /*
            if (contextRef != null && contextRef.get() != null) {
                AppDatabase db = Room.databaseBuilder(contextRef.get(), AppDatabase.class, "cities").build();
                List<CityEntity> cityEntities = db.cityDao().getAll();
                if (cityEntities.size() > 0) {
                    for (CityEntity city : cityEntities) {
                        updateCity(city);
                    }
                }
            }*/
   //     });
       // return cities;
    }

    @Override
    public void addNewCity(String cityName) {
        if (!cityExists(cityName)) {
            notMainThread.execute(() -> {
                retrofit2.Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
                GetDataService service = retrofit.create(GetDataService.class);
                Call<WeatherObject> call = service.getWeatherForCity(Constants.API_KEY, cityName);
                call.enqueue(new Callback<WeatherObject>() {
                    @Override
                    public void onResponse(Call<WeatherObject> call, Response<WeatherObject> response) {
                        receiveWeatherForCity(response.body());
                    }
                    @Override
                    public void onFailure(Call<WeatherObject> call, Throwable t) {
                    }
                });
            });
        }
    }

    @Override
    public void updateCity(CityEntity city) {
        retrofit2.Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService service = retrofit.create(GetDataService.class);
        Call<WeatherObject> call = service.getWeatherForCity(Constants.API_KEY, city.getName());
        call.enqueue(new Callback<WeatherObject>() {
            @Override
            public void onResponse(Call<WeatherObject> call, Response<WeatherObject> response) {
                WeatherObject weatherObject = response.body();
                updateCityInDatabase(weatherObject);
                fragment.showCityLoaded(CityEntityAdapter.convertCityEntityToCity(city));
                weatherObjects.add(weatherObject);
            }
            @Override
            public void onFailure(Call<WeatherObject> call, Throwable t) {
            }
        });
    }

    @Override
    public WeatherObject getWeatherObjectOnIndex(int index) {
        return weatherObjects.get(index);
    }

    @Override
    public void deleteCity(City city) {

    }

    private boolean cityExists(String cityName) {
        for (City c : cities) {
            if (c.getName().equals(cityName)) {
                fragment.showErrorAddingAddedCity();
                return true;
            }
        }
        return false;
    }

    private void receiveWeatherForCity(WeatherObject weather) {
        notMainThread.execute(() -> {
            CityEntity city = CityEntityAdapter.convertWeatherObjectToCityEntity(weather);
            addInDatabase(city);
        });
    }

    private void updateCityInDatabase(WeatherObject w) {
        notMainThread.execute(() -> {
            if (contextRef != null && contextRef.get() != null) {
                AppDatabase db = Room.databaseBuilder(contextRef.get(), AppDatabase.class, "cities").build();
                int iconId = ImageOperator.getImageIdFromString(w.getWeather().get(0).getIcon());
                db.cityDao().updateCity(w.getName(), w.getMain().getTemp(), iconId);
            }
        });
    }

    private void addInDatabase(CityEntity cityEntity) {
        if (contextRef != null && contextRef.get() != null) {
            AppDatabase db =
                    Room.databaseBuilder(contextRef.get(), AppDatabase.class, "cities").build();
            if (db.cityDao().getCity(cityEntity.getName()) == null) {
                db.cityDao().insertCity(cityEntity);
                fragment.showNewCityAdded(CityEntityAdapter.convertCityEntityToCity(cityEntity));
            } else {
                db.cityDao().updateCity(cityEntity.getName(), cityEntity.getLastTemperature(), cityEntity.getLastImageId());
            }
        }
    }
}
