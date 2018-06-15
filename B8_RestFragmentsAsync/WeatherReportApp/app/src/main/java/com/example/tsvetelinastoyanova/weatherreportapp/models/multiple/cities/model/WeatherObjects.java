
package com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherObjects {

    @SerializedName("cnt")
    @Expose
    private int cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<WeatherObject> weatherObject = null;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public java.util.List<WeatherObject> getWeatherObject() {
        return weatherObject;
    }

    public void setWeatherObject(java.util.List<WeatherObject> weatherObject) {
        this.weatherObject = weatherObject;
    }

}
