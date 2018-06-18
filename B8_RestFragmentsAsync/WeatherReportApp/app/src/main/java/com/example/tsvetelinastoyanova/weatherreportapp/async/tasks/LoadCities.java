package com.example.tsvetelinastoyanova.weatherreportapp.async.tasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.tsvetelinastoyanova.weatherreportapp.ImageOperator;
import com.example.tsvetelinastoyanova.weatherreportapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.weatherreportapp.database.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObjects;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class LoadCities extends AsyncTask<Context, Void, String> {
    private List<WeatherObject> objects = new ArrayList<>();
    AsyncTaskOperationsHandler handler = new AsyncTaskOperationsHandler();
    Context context;

    private LoadCitiesDelegate loadCitiesDelegate;

    public interface LoadCitiesDelegate {
        void onLoadingCitiesEndWithResult(boolean success);

        void onLoadingCitiesFinishGettingData(List<WeatherObject> weatherObjects);
    }

    public void setLoadCitiesDelegate(LoadCitiesDelegate loadCitiesDelegate) {
        this.loadCitiesDelegate = loadCitiesDelegate;
    }

    @Override
    protected String doInBackground(Context... contexts) {
        this.context = contexts[0];
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "cities").build();
        List<CityEntity> cityEntities = db.cityDao().getAll();
        String ids = "";
        if (cityEntities.size() == 0) {
            loadCitiesDelegate.onLoadingCitiesEndWithResult(false);
        } else {
            //  The limit of locations is 20.
            //  In cycle, we separate cityEntities list to make call to only 20 locations
            // NOTE: A single ID counts as a one API call! So, the above example is treated as a 3 API calls.
            for (int i = 0; i < cityEntities.size()/20*20; i += 20) {
                ids = buildStringFromIds(cityEntities.subList(i, i + 19));
                connectToApi(ids);
            }
            // the rest locations, less than 20
            // we get from cityEntities.size() / 20*20-1 to
            ids = buildStringFromIds(cityEntities.subList(cityEntities.size() / 20*20-1, cityEntities.size()));
        }
        connectToApi(ids);
        return "";
    }

    private String buildStringFromIds(List<CityEntity> cityEntities) {
        String ids = "";
        for (CityEntity city : cityEntities) {
            ids = ids.concat(city.getCityId() + ",");
        }
        return ids.substring(0, ids.length() - 1);
    }

    private void connectToApi(String cityIds) {
        URL url = handler.getConnectionToLoadWeatherForMultipleCities(cityIds);
        HttpURLConnection urlConnection = handler.getConnection(url);
        WeatherObjects weatherObjects = handler.getManyWeatherObjectsFromInputStream(urlConnection);
        if (weatherObjects != null) {
            for (WeatherObject w : weatherObjects.getWeatherObject()) {
                objects.add(w);
                updateDataInDatabase(w);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    @Override
    protected void onPostExecute(String values) {
        loadCitiesDelegate.onLoadingCitiesFinishGettingData(objects);
        loadCitiesDelegate.onLoadingCitiesEndWithResult(true);
    }

    private void updateDataInDatabase(WeatherObject w) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "cities").build();
        int iconId = ImageOperator.getImageIdFromString(w.getWeather().get(0).getIcon());
        db.cityDao().updateCity(w.getName(), w.getMain().getTemp(), iconId);
    }

}
