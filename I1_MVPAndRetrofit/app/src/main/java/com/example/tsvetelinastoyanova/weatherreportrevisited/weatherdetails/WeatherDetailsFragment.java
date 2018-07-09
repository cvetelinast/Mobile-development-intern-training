package com.example.tsvetelinastoyanova.weatherreportrevisited.weatherdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.weatherreportrevisited.Constants;
import com.example.tsvetelinastoyanova.weatherreportrevisited.R;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.ImageOperator;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;


public class WeatherDetailsFragment extends Fragment implements WeatherDetailsContract.View {

    private WeatherDetailsContract.Presenter presenter;

    public static WeatherDetailsFragment newInstance() {
        return new WeatherDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        return view;
    }

    @Override
    public void setPresenter(WeatherDetailsContract.Presenter presenter) {
        this.presenter = Utils.checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.start();
        }
    }

    @Override
    public void changeView(WeatherObject weatherObject) {
        TextView cityAndCountry = getView().findViewById(R.id.city_and_country);
        String city = weatherObject.getName();
        String country = weatherObject.getSys().getCountry();
        cityAndCountry.setText(String.format(Constants.COUNTRY_CITY, city, country));

        ImageView icon = getView().findViewById(R.id.icon);
        int id = ImageOperator.getImageIdFromString(weatherObject.getWeather().get(0).getIcon());
        icon.setImageDrawable(getActivity().getResources().getDrawable(id));

        TextView degrees = getView().findViewById(R.id.degrees);
        degrees.setText(String.format(Constants.DEGREES_CELSIUS, weatherObject.getMain().getTemp()));

        TextView weather = getView().findViewById(R.id.weather);
        weather.setText(weatherObject.getWeather().get(0).getDescription());

        TextView tempMin = getView().findViewById(R.id.temp_min);
        tempMin.setText(getResources().getString(R.string.temperature_min, String.format(Constants.DEGREES_CELSIUS, weatherObject.getMain().getTempMin())));

        TextView tempMax = getView().findViewById(R.id.temp_max);
        tempMax.setText(getResources().getString(R.string.temperature_max, String.format(Constants.DEGREES_CELSIUS, weatherObject.getMain().getTempMax())));

        TextView pressure = getView().findViewById(R.id.pressure);
        pressure.setText(getResources().getString(R.string.pressure, weatherObject.getMain().getPressure()));

        TextView humidity = getView().findViewById(R.id.humidity);
        humidity.setText(getResources().getString(R.string.humidity, weatherObject.getMain().getHumidity()));

        TextView windSpeed = getView().findViewById(R.id.wind_speed);
        windSpeed.setText(getResources().getString(R.string.wind_speed, weatherObject.getWind().getSpeed()));
    }
}
