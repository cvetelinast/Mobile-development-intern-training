package com.example.tsvetelinastoyanova.weatherreportrevisited;

public interface Constants {
    String DEGREES_CELSIUS = "%s °C";
    String IMG_URL = "http://openweathermap.org/img/w/";
    String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=956b0cec24dc9a86dddab6ed85a14dd5";
    String WEATHER_MULTIPLE_CITIES_URL = "http://api.openweathermap.org/data/2.5/group?id=%s&APPID=956b0cec24dc9a86dddab6ed85a14dd5";
    String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s,%s&APPID=956b0cec24dc9a86dddab6ed85a14dd5";
    String COUNTRY_CITY = "%s, %s";
    String WEATHER_OBJECTS = "weatherObjects";
    String API_KEY = "956b0cec24dc9a86dddab6ed85a14dd5";
}
