package com.example.tsvetelinastoyanova.weatherreportapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.weatherreportapp.Constants;
import com.example.tsvetelinastoyanova.weatherreportapp.ImageOperator;
import com.example.tsvetelinastoyanova.weatherreportapp.R;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObject;


public class WeatherDetailsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_details, container, false);
    }

    public void changeView(WeatherObject object){
        TextView cityAndCountry = getView().findViewById(R.id.city_and_country);
        String city = object.getName();
        String country = object.getSys().getCountry();
        cityAndCountry.setText(String.format(Constants.COUNTRY_CITY,city, country));

        ImageView icon = getView().findViewById(R.id.icon);
        int id = ImageOperator.getImageIdFromString(object.getWeather().get(0).getIcon());
        icon.setImageDrawable(getActivity().getResources().getDrawable(id));

        TextView degrees =  getView().findViewById(R.id.degrees);
        degrees.setText(String.format(Constants.DEGREES_CELSIUS,object.getMain().getTemp()));

        TextView weather = getView().findViewById(R.id.weather);
        weather.setText(object.getWeather().get(0).getMain());

        TextView tempMin =  getView().findViewById(R.id.temp_min);
        tempMin.setText(getResources().getString(R.string.temperature_min,String.format(Constants.DEGREES_CELSIUS,object.getMain().getTempMin())));

        TextView tempMax =  getView().findViewById(R.id.temp_max);
        tempMax.setText(getResources().getString(R.string.temperature_max,String.format(Constants.DEGREES_CELSIUS,object.getMain().getTempMax())));

        TextView pressure =  getView().findViewById(R.id.pressure);
        pressure.setText(getResources().getString(R.string.pressure, object.getMain().getPressure()));

        TextView humidity =  getView().findViewById(R.id.humidity);
        humidity.setText(getResources().getString(R.string.humidity,object.getMain().getHumidity()));

        TextView windSpeed =  getView().findViewById(R.id.wind_speed);
        windSpeed.setText(getResources().getString(R.string.wind_speed,object.getWind().getSpeed()));

    }

}
