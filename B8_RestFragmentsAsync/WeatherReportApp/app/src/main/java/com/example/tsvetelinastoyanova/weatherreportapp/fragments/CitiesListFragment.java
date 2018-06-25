package com.example.tsvetelinastoyanova.weatherreportapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tsvetelinastoyanova.weatherreportapp.visualization.CitiesAdapter;
import com.example.tsvetelinastoyanova.weatherreportapp.City;
import com.example.tsvetelinastoyanova.weatherreportapp.ImageOperator;
import com.example.tsvetelinastoyanova.weatherreportapp.R;
import com.example.tsvetelinastoyanova.weatherreportapp.async.tasks.AddNewCity;
import com.example.tsvetelinastoyanova.weatherreportapp.async.tasks.LoadCities;
import com.example.tsvetelinastoyanova.weatherreportapp.models.multiple.cities.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportapp.visualization.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CitiesListFragment extends Fragment implements LoadCities.LoadCitiesDelegate, AddNewCity.AddNewCityDelegate {
    OnHeadlineSelectedListener activityCallback;
    private List<City> citiesList = new ArrayList<>();
    private List<WeatherObject> weatherObjects = new ArrayList<>();
    private CitiesAdapter adapter;
    private TextInputLayout cityNameContainer;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onWeatherObjectsLoaded(List<WeatherObject> weatherObjects);
        public void onWeatherObjectClicked(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            activityCallback = (OnHeadlineSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onLoadingCitiesEndWithResult(boolean success) {
        if (success) {
            activityCallback.onWeatherObjectsLoaded(this.weatherObjects);
        }
    }

    @Override
    public void onAddingNewCityEndWithResult(boolean success) {

    }

    @Override
    public void onAddingNewCityFinishGettingData(WeatherObject result) {
        if (result != null) {
            int id = ImageOperator.getImageIdFromString(result.getWeather().get(0).getIcon());
            this.citiesList.add(new City(result.getName(), result.getMain().getTemp(), id));
            adapter.notifyDataSetChanged();
            weatherObjects.add(result);
        }
    }

    void addNewCity(String city) {
        AddNewCity task = new AddNewCity(getActivity().getApplicationContext());
        task.setTaskDelegate(this);
        task.execute(city);
    }

    @Override
    public void onLoadingCitiesFinishGettingData(List<WeatherObject> weatherObjects) {
        if (weatherObjects.size() != 0) {
            this.weatherObjects.addAll(weatherObjects);
            for (WeatherObject w : weatherObjects) {
                int id = ImageOperator.getImageIdFromString(w.getWeather().get(0).getIcon());
                this.citiesList.add(new City(w.getName(), w.getMain().getTemp(), id));
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities_list, container, false);

        addClickListenerToAddCityButton(view.findViewById(R.id.add_city_button));
        setTextContainer(view.findViewById(R.id.new_city_wrapper));
        createRecyclerView(view.findViewById(R.id.recycler_view));
        fillRecyclerView();
        return view;
    }

    private void setTextContainer(TextInputLayout t) {
        cityNameContainer = t;
    }

    private void addClickListenerToAddCityButton(Button addCityButton) {
        addCityButton.setOnClickListener((v) -> {
            buttonToAddNewCityClicked();
        });
    }

    private void createRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        OnItemClickListener onItemClickListener = (view, position) -> activityCallback.onWeatherObjectClicked(position);
        adapter = new CitiesAdapter(citiesList, onItemClickListener);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void fillRecyclerView() {
        LoadCities task = new LoadCities(getActivity().getApplicationContext());
        task.setLoadCitiesDelegate(this);
        task.execute();
    }

    private void buttonToAddNewCityClicked() {
        String name = cityNameContainer.getEditText().getText().toString();
        if (!name.isEmpty() && !listContainsCity(citiesList, name)) {
            addNewCity(name);
        }
    }

    private boolean listContainsCity(List<City> citiesList, String name) {
        for (City c : citiesList) {
            if (c.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
