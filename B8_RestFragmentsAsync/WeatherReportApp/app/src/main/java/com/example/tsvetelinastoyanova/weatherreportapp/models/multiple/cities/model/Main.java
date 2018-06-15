
package com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    @Expose
    private double temp;
    @SerializedName("pressure")
    @Expose
    private int pressure;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("temp_min")
    @Expose
    private double tempMin;
    @SerializedName("temp_max")
    @Expose
    private double tempMax;

    public double getTemp() {
        return convertTemperatureToCelsius(temp);
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getTempMin() {
        return convertTemperatureToCelsius(tempMin);
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return convertTemperatureToCelsius(tempMax);
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double convertTemperatureToCelsius(double temp) {
        return Math.round((temp - 273.15) * 10) / 10.0;
    }

}
