package com.example.tsvetelinastoyanova.weatherreportapp;


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
                id = R.drawable.clouds_rain;
                break;
            case "10d":
                id = R.drawable.cloud_sun_rain;
                break;
            case "11d":
                id = R.drawable.storm;
                break;
            case "13d":
                id = R.drawable.snow;
                break;
            case "01n":
                id = R.drawable.brown;
                break;
            case "02n":
                id=R.drawable.cloud_brown;
                break;
            default:
                id = R.drawable.ic_launcher_background;
                break;
        }
        return id;
    }
}
