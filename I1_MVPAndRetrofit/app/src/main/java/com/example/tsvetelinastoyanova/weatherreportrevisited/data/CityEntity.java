package com.example.tsvetelinastoyanova.weatherreportrevisited.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "cities")
public class CityEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "cityId")
    private int cityId;

    @ColumnInfo(name = "lastTemperature")
    private double lastTemperature;

    @ColumnInfo(name = "lastImageId")
    private int lastImageId;

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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public double getLastTemperature() {
        return lastTemperature;
    }

    public void setLastTemperature(double lastTemperature) {
        this.lastTemperature = lastTemperature;
    }

    public int getLastImageId() {
        return lastImageId;
    }

    public void setLastImageId(int lastImageId) {
        this.lastImageId = lastImageId;
    }
}
