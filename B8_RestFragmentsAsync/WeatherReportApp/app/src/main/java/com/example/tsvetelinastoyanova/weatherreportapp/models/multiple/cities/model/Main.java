
package com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.temp);
        dest.writeInt(this.pressure);
        dest.writeInt(this.humidity);
        dest.writeDouble(this.tempMin);
        dest.writeDouble(this.tempMax);
    }

    public Main() {
    }

    protected Main(Parcel in) {
        this.temp = in.readDouble();
        this.pressure = in.readInt();
        this.humidity = in.readInt();
        this.tempMin = in.readDouble();
        this.tempMax = in.readDouble();
    }

    public static final Parcelable.Creator<Main> CREATOR = new Parcelable.Creator<Main>() {
        @Override
        public Main createFromParcel(Parcel source) {
            return new Main(source);
        }

        @Override
        public Main[] newArray(int size) {
            return new Main[size];
        }
    };
}
