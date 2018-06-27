package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.R;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization.CitiesAdapter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization.OnItemClickListener;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.ActivityUtils;

public class CitiesListFragment extends Fragment implements CitiesListContract.View {
    CitiesAdapter citiesAdapter;
    RecyclerView recyclerView;
    private CitiesListContract.Presenter presenter;
    private TextInputLayout cityNameContainer;

    @Override
    public void setPresenter(CitiesListContract.Presenter presenter) {
        this.presenter = ActivityUtils.checkNotNull(presenter);
    }

    public static CitiesListFragment newInstance() {
        return new CitiesListFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.start();
        }
    }

    private void createRecyclerView(RecyclerView recyclerView) {
        OnItemClickListener onItemClickListener = (view, position) -> Log.d("tag","clicked on item on position " + position);
        citiesAdapter = new CitiesAdapter(presenter, onItemClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(citiesAdapter);
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
        recyclerView = view.findViewById(R.id.recycler_view);
        createRecyclerView(recyclerView);
        return view;
    }

    private void addClickListenerToAddCityButton(Button addCityButton) {
        addCityButton.setOnClickListener((v) -> presenter.addNewCity(cityNameContainer.getEditText().getText().toString()));
    }

    private void setTextContainer(TextInputLayout t) {
        cityNameContainer = t;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showNewCityAdded(City newCity) {
        citiesAdapter.addNewCityToShow(newCity);
        citiesAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), R.string.added_city, Toast.LENGTH_SHORT).show();
        cityNameContainer.getEditText().setText("");
    }

    @Override
    public void showCityDeleted(City deletedCity) {

    }
}
