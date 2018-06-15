package com.example.tsvetelinastoyanova.weatherreportapp.async.tasks;

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

public class AsyncTaskOperationsHandler {

    URL getConnectionToLoadWeatherForMultipleCities(String cityIds) {
        try {
            return new URL(String.format(Constants.WEATHER_MULTIPLE_CITIES_URL, cityIds));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    URL getConnectionToLoadWeatherForSingleCity(String cityIds) {
        try {
            return new URL(String.format(Constants.WEATHER_URL, cityIds));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    HttpURLConnection getConnection(URL url) {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    WeatherObjects getManyWeatherObjectsFromInputStream(HttpURLConnection urlConnection) {
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String json = convertStreamToString(in);
            Gson gson = new Gson();
            return gson.fromJson(json, WeatherObjects.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
            }
        }
        return null;
    }

    WeatherObject getSingleWeatherReportObjectFromInputStream(HttpURLConnection urlConnection) {
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String json = convertStreamToString(in);
            Gson gson = new Gson();
            return gson.fromJson(json, WeatherObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
            }
        }
        return null;
    }

    private static String convertStreamToString(InputStream in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
