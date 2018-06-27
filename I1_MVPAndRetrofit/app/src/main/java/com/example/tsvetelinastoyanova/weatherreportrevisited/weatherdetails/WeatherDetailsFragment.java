package com.example.tsvetelinastoyanova.weatherreportrevisited.weatherdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tsvetelinastoyanova.weatherreportrevisited.R;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.ActivityUtils;


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
        return inflater.inflate(R.layout.fragment_weather_details, container, false);
    }

    @Override
    public void setPresenter(WeatherDetailsContract.Presenter presenter) {
        this.presenter = ActivityUtils.checkNotNull(presenter);
        Log.d("tag", "WeatherDetailsFragment set presenter");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }
}
