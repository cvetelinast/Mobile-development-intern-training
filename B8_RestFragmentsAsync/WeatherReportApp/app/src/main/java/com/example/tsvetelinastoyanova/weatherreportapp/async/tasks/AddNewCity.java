package com.example.tsvetelinastoyanova.weatherreportapp.async.tasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.tsvetelinastoyanova.weatherreportapp.ImageOperator;
import com.example.tsvetelinastoyanova.weatherreportapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.weatherreportapp.database.CityEntity;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class AddNewCity extends AsyncTask<String, Void, String> {
    private WeatherObject object;
    private AsyncTaskOperationsHandler handler = new AsyncTaskOperationsHandler();
    private Context context;

    private AddNewCityDelegate taskDelegate;

    public interface AddNewCityDelegate {
        void onAddingNewCityEndWithResult(boolean success);

        void onAddingNewCityFinishGettingData(WeatherObject result);
    }

    public void setTaskDelegate(AddNewCityDelegate taskDelegate) {
        this.taskDelegate = taskDelegate;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... city) {
        URL url = handler.getConnectionToLoadWeatherForSingleCity(city[0]);
        HttpURLConnection urlConnection = handler.getConnection(url);
        this.object = handler.getSingleWeatherReportObjectFromInputStream(urlConnection);
        if (this.object != null) {
            addInDatabase(this.object);
        }
        return "";
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    private void addInDatabase(WeatherObject object) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "cities").build();
        CityEntity c = new CityEntity();
        c.setName(object.getName());
        c.setCityId(object.getId());
        c.setLastTemperature(object.getMain().getTemp());
        String imageId = object.getWeather().get(0).getIcon();
        c.setLastImageId(ImageOperator.getImageIdFromString(imageId));
        db.cityDao().insertCity(c);
    }

    @Override
    protected void onPostExecute(String dummyString) {
        taskDelegate.onAddingNewCityFinishGettingData(object);
        taskDelegate.onAddingNewCityEndWithResult(true);
    }
}
