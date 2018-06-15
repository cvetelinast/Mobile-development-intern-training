package com.example.tsvetelinastoyanova.weatherreportapp;

public class City {

    String name;
    double temperature;
    int iconId;

    public City(String name, double temperature, int iconId) {
        this.name = name;
        this.temperature = temperature;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

}
