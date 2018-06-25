package com.example.tsvetelinastoyanova.weatherreportapp.async.tasks;

import android.util.Log;

import com.example.tsvetelinastoyanova.weatherreportapp.Constants;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObjects;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class AsyncTaskUtils {

    static URL getConnectionToLoadWeatherForMultipleCities(String cityIds) {
        try {
            return new URL(String.format(Constants.WEATHER_MULTIPLE_CITIES_URL, cityIds));
        } catch (MalformedURLException e) {
            Log.d("Error", "There was a problem with the url " + String.format(Constants.WEATHER_MULTIPLE_CITIES_URL, cityIds));
            e.printStackTrace();
        }
        return null;
    }

    static URL getConnectionToLoadWeatherForSingleCity(String cityId) {
        if (cityId == null) {
            return null;
        }
        try {
            return new URL(String.format(Constants.WEATHER_URL, cityId));
        } catch (MalformedURLException e) {
            Log.d("Error", "There was a problem with the url " + String.format(Constants.WEATHER_URL, cityId));
            e.printStackTrace();
        }
        return null;
    }

    static HttpURLConnection getConnection(URL url) {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.d("Error", "There was a problem with the HttpURLConnection to " + url);
            e.printStackTrace();
        }
        return null;
    }

    static WeatherObjects getManyWeatherObjectsFromInputStream(HttpURLConnection urlConnection) {
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String json = convertStreamToString(in);
            Gson gson = new Gson();
            return gson.fromJson(json, WeatherObjects.class);
        } catch (Exception e) {
            Log.d("Error", "There was a problem with opening url connection.");
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("Error", "A problem with disconnecting occurred");
            }
        }
        return null;
    }

    static WeatherObject getSingleWeatherReportObjectFromInputStream(HttpURLConnection urlConnection) {
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String json = convertStreamToString(in);
            Gson gson = new Gson();
            return gson.fromJson(json, WeatherObject.class);
        } catch (Exception e) {
            Log.d("Error", "There was a problem with opening url connection.");
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("Error", "A problem with disconnecting occurred");
            }
        }
        return null;
    }

    private static String convertStreamToString(InputStream in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
