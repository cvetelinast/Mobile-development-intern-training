
package com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tsvetelinastoyanova.weatherreportapp.ImageOperator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherObject implements Parcelable {

    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("weather")
    @Expose
    private java.util.List<Weather> weather = null;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("visibility")
    @Expose
    private int visibility;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("dt")
    @Expose
    private int dt;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public java.util.List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.coord, flags);
        dest.writeParcelable(this.sys, flags);
        dest.writeList(this.weather);
        dest.writeParcelable(this.main, flags);
        dest.writeInt(this.visibility);
        dest.writeParcelable(this.wind, flags);
        dest.writeParcelable(this.clouds, flags);
        dest.writeInt(this.dt);
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public WeatherObject() {
    }

    protected WeatherObject(Parcel in) {
        this.coord = in.readParcelable(Coord.class.getClassLoader());
        this.sys = in.readParcelable(Sys.class.getClassLoader());
        this.weather = new ArrayList<Weather>();
        in.readList(this.weather, Weather.class.getClassLoader());
        this.main = in.readParcelable(Main.class.getClassLoader());
        this.visibility = in.readInt();
        this.wind = in.readParcelable(Wind.class.getClassLoader());
        this.clouds = in.readParcelable(Clouds.class.getClassLoader());
        this.dt = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<WeatherObject> CREATOR = new Parcelable.Creator<WeatherObject>() {
        @Override
        public WeatherObject createFromParcel(Parcel source) {
            return new WeatherObject(source);
        }

        @Override
        public WeatherObject[] newArray(int size) {
            return new WeatherObject[size];
        }
    };
}
