/*
package com.example.tsvetelinastoyanova.weatherreportapp.async.tasks;

import android.os.AsyncTask;

import com.example.tsvetelinastoyanova.weatherreportapp.Constants;
import com.example.tsvetelinastoyanova.weatherreportapp.models.WeatherReportObject;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class AsyncTaskAPI extends AsyncTask<String, Integer, String> {

    // Runs in UI before background thread is called
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Do something like display a progress bar
    }

    // This is run in a background thread
    @Override
    protected WeatherReportObject doInBackground(String... params) {
        // get the string from params, which is an array
        String city = params[0];
        String country = params[1];

        URL url = null;
        try {
            url = new URL(String.format(Constants.WEATHER_URL, city, "bg"));
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Sofia,bg&APPID=956b0cec24dc9a86dddab6ed85a14dd5");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String json = convertStreamToString(in);
            Gson gson = new Gson();
            WeatherReportObject weatherReportObject = gson.fromJson(json, WeatherReportObject.class);
            return weatherReportObject;
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

    // Do things

    // Call this to update your progress
  */
/*  publishProgress(i);
}*//*

*/
/*

        return"this string is passed to onPostExecute";
}
*//*


    private static String convertStreamToString(InputStream in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";

    }

    // This is called from background thread but runs in UI
    @Override
    protected void onProgressUpdate(WeatherReportObject... values) {
        super.onProgressUpdate(values);

        // Do things like update the progress bar
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Do things like hide the progress bar or change a TextView
    }
}
*/
