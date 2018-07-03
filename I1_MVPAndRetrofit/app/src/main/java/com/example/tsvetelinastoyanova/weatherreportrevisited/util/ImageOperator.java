package com.example.tsvetelinastoyanova.weatherreportrevisited.util;


import com.example.tsvetelinastoyanova.weatherreportrevisited.R;

public class ImageOperator {

    public static int getImageIdFromString(String idString) {
        int id = 0;
        switch (idString) {
            case "01d":
                id = R.drawable.sun;
                break;
            case "02d":
                id = R.drawable.sun_cloud;
                break;
            case "03d":
            case "03n":
                id = R.drawable.cloud;
                break;
            case "04d":
            case "04n":
                id = R.drawable.clouds;
                break;
            case "09d":
            case "09n":
                id = R.drawable.clouds_rain;
                break;
            case "10d":
                id = R.drawable.cloud_sun_rain;
                break;
            case "11d":
            case "11n":
                id = R.drawable.storm;
                break;
            case "13d":
            case "13n":
                id = R.drawable.snow;
                break;
            case "50d":
            case "50n":
                id = R.drawable.mist;
                break;
            case "01n":
                id = R.drawable.brown;
                break;
            case "02n":
                id = R.drawable.cloud_brown;
                break;
            case "10n":
                id = R.drawable.cloud_moon_rain;
                break;
            default:
                id = R.drawable.ic_launcher_background;
                break;
        }
        return id;
    }
}
