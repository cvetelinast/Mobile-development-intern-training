package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.R;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization.CitiesAdapter;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization.OnItemClickListener;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization.OnSwipeTouchListener;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;

public class CitiesListFragment extends Fragment implements CitiesListContract.View {
    CitiesAdapter citiesAdapter;
    RecyclerView recyclerView;
    private CitiesListContract.Presenter presenter;
    private TextInputLayout cityNameContainer;

    /*** interface ***/

    private OnClickCityDelegate onClickCityDelegate;

    public interface OnClickCityDelegate {
        void onClickCity(WeatherObject weatherObject);
    }

    /*** Methods from Fragment ***/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onClickCityDelegate = (OnClickCityDelegate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
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
        recyclerView = view.findViewById(R.id.recycler_view);
        createRecyclerView(recyclerView);


        presenter.start();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.start();
        }
    }

    /*** Methods from CitiesListContract.View ***/

    @Override
    public void setWeatherObjectWhenClicked(WeatherObject weatherObject) {
        onClickCityDelegate.onClickCity(weatherObject);
    }

    @Override
    public void setPresenter(CitiesListContract.Presenter presenter) {
        this.presenter = Utils.checkNotNull(presenter);
    }

    @Override
    public void showNewCityAdded(City newCity) {
        getActivity().runOnUiThread(() -> {
            citiesAdapter.addNewCityToShow(newCity);
            citiesAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), R.string.added_city, Toast.LENGTH_SHORT).show();
            cityNameContainer.getEditText().setText("");
        });
    }

    @Override
    public void showErrorAddingAddedCity() {
        Toast.makeText(getContext(), R.string.existing_city, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCityDeleted(City deletedCity) {

    }

    @Override
    public void showCityLoaded(City city) {
    //    getActivity().runOnUiThread(() -> {
            citiesAdapter.addNewCityToShow(city);
            citiesAdapter.notifyDataSetChanged();
     //   });
    }

    public static CitiesListFragment newInstance() {
        return new CitiesListFragment();
    }

    private void addClickListenerToAddCityButton(Button addCityButton) {
        addCityButton.setOnClickListener((v) -> presenter.addNewCity(cityNameContainer.getEditText().getText().toString()));
    }

    private void setTextContainer(TextInputLayout t) {
        cityNameContainer = t;
    }

    private void createRecyclerView(RecyclerView recyclerView) {
        OnItemClickListener onItemClickListener = (view, position) -> setWeatherObjectWhenClicked(presenter.getWeatherObjectOnIndex(position));
     //   OnSwipeTouchListener onSwipeTouchListener = (view, position) ->setOnSwipeTouchListener(presenter.getWeatherObjectOnIndex(position));
      //  citiesAdapter = new CitiesAdapter(presenter, onItemClickListener, onSwipeTouchListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(citiesAdapter);
    }

    private OnSwipeTouchListener setOnSwipeTouchListener(WeatherObject weatherObject){
        return new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(getActivity(), "bottom", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
