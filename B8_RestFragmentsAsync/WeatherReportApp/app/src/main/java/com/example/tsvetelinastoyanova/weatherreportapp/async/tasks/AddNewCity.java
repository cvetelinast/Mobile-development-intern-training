package com.example.tsvetelinastoyanova.weatherreportapp.async.tasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportapp.ImageOperator;
import com.example.tsvetelinastoyanova.weatherreportapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.weatherreportapp.database.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObject;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Nullable;

public class AddNewCity extends AsyncTask<String, Void, WeatherObject> {
    private WeakReference<Context> contextRef;

    private AddNewCityDelegate taskDelegate;

    private boolean doesCityExist;

    public interface AddNewCityDelegate {

        void onAddingNewCityEndWithResult(boolean success);

        void onAddingNewCityFinishGettingData(WeatherObject result);
    }

    public AddNewCity(Context context) {
        contextRef = new WeakReference<>(context);
    }

    public void setTaskDelegate(AddNewCityDelegate taskDelegate) {
        this.taskDelegate = taskDelegate;
    }

    @Override
    protected WeatherObject doInBackground(String... city) {
        URL url = AsyncTaskUtils.getConnectionToLoadWeatherForSingleCity(city[0]);
        HttpURLConnection urlConnection = AsyncTaskUtils.getConnection(url);
        WeatherObject object = AsyncTaskUtils.getSingleWeatherReportObjectFromInputStream(urlConnection);
        if (object != null) {
            addInDatabase(object);
        }
        return object;
    }

    @Override
    protected void onPostExecute(@Nullable WeatherObject object) {
        if (!doesCityExist) {
            taskDelegate.onAddingNewCityFinishGettingData(object);
            taskDelegate.onAddingNewCityEndWithResult(true);
        }
    }

    private void addInDatabase(WeatherObject object) {
        if (contextRef != null && contextRef.get() != null) {
            AppDatabase db =
                    Room.databaseBuilder(contextRef.get(), AppDatabase.class, "cities").build();
            CityEntity c = setAttributesToCityEntity(object);
            if (db.cityDao().getCity(object.getName()) == null) {
                db.cityDao().insertCity(c);
            } else {
                doesCityExist = true;
                db.cityDao().updateCity(c.getName(), c.getLastTemperature(), c.getLastImageId());
            }
        }
    }

    private CityEntity setAttributesToCityEntity(WeatherObject object) {
        CityEntity c = new CityEntity();
        c.setName(object.getName());
        c.setCityId(object.getId());
        c.setLastTemperature(object.getMain().getTemp());
        String imageId = object.getWeather().get(0).getIcon();
        c.setLastImageId(ImageOperator.getImageIdFromString(imageId));
        return c;
    }

}
