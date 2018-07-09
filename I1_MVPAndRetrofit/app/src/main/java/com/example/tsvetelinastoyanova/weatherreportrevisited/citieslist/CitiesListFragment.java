package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization.RecyclerItemTouchHelper;
import com.example.tsvetelinastoyanova.weatherreportrevisited.model.WeatherObject;
import com.example.tsvetelinastoyanova.weatherreportrevisited.util.Utils;

import java.util.List;


public class CitiesListFragment extends Fragment implements CitiesListContract.View,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private CitiesAdapter citiesAdapter;
    private CitiesListContract.Presenter presenter;
    private TextInputLayout cityNameContainer;
    private CoordinatorLayout coordinatorLayout;

    private boolean isFirstTimeLoading = true;

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
            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities_list, container, false);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        addClickListenerToAddCityButton(view.findViewById(R.id.add_city_button));
        setTextContainer(view.findViewById(R.id.new_city_wrapper));
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        createRecyclerView(recyclerView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.checkNotNull(presenter);
        if (isFirstTimeLoading) {
            isFirstTimeLoading = false;
            presenter.start();
        }
    }

    /*** Methods from CitiesListContract.View ***/

    @Override
    public void setWeatherObjectWhenClicked(String cityName) {
        WeatherObject weatherObject = presenter.getWeatherObjectOnClick(cityName);
        if (weatherObject != null) { // if click while refreshing
            onClickCityDelegate.onClickCity(weatherObject);
        }
    }

    @Override
    public void setPresenter(CitiesListContract.Presenter presenter) {
        this.presenter = Utils.checkNotNull(presenter);
    }

    @Override
    public void showNewCityAdded(City newCity) {
        citiesAdapter.addNewCityToShow(newCity);
        Toast.makeText(getContext(), R.string.added_city, Toast.LENGTH_SHORT).show();
        cityNameContainer.getEditText().setText("");
    }

    @Override
    public void showErrorAddingAddedCity() {
        Toast.makeText(getContext(), R.string.not_added_city, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorLoadingCities() {
        Toast.makeText(getContext(), R.string.problem_loading_cities, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCityDeleted() {
        Toast.makeText(getContext(), R.string.city_deleted_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorDeleteCity() {
        Toast.makeText(getContext(), R.string.problem_deleting_cities, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCityLoaded(City city) {
        citiesAdapter.addNewCityToShow(city);
    }

    @Override
    public void showCityUpdated(City newCity) {
        citiesAdapter.refreshCity(newCity);
    }

    public static CitiesListFragment newInstance() {
        return new CitiesListFragment();
    }

    @Override
    public List<City> getDisplayedCities() {
        return citiesAdapter.getCitiesList();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CitiesAdapter.MyViewHolder) {
            String currentCityName = citiesAdapter.getCityNameOnIndex(position);

            // backup of removed item for undo purpose
            final City deletedItem = citiesAdapter.getCityOnIndex(position);
            final int deletedIndex = viewHolder.getAdapterPosition();

            citiesAdapter.removeCity(viewHolder.getAdapterPosition());

            showSnackbarWithUndo(currentCityName, deletedItem, deletedIndex);
        }
    }

    private void showSnackbarWithUndo(String name, City deletedItem, int deletedIndex) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getResources().getString(R.string.city_removed_message, name), Snackbar.LENGTH_LONG);

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_TIMEOUT) {
                    presenter.deleteCity(deletedItem);
                }
            }
        }).setAction(R.string.undo, ((view) ->  // undo is selected, restore the deleted item
                citiesAdapter.restoreCity(deletedItem, deletedIndex)
        ));

        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void addClickListenerToAddCityButton(Button addCityButton) {
        addCityButton.setOnClickListener((v) -> presenter.addNewCity(cityNameContainer.getEditText().getText().toString()));
    }

    private void setTextContainer(TextInputLayout t) {
        cityNameContainer = t;
    }

    private void createRecyclerView(RecyclerView recyclerView) {
        OnItemClickListener onItemClickListener = (view, position) -> setWeatherObjectWhenClicked(citiesAdapter.getCityNameOnIndex(position));
        citiesAdapter = new CitiesAdapter(onItemClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(citiesAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

}
