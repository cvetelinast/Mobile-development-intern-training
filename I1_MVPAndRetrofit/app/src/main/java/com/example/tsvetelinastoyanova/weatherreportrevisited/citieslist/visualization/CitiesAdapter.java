package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.Constants;
import com.example.tsvetelinastoyanova.weatherreportrevisited.R;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.CitiesListContract;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder> {

    private List<City> citiesList;
    private OnItemClickListener onItemClickListener;
    private final CitiesListContract.Presenter presenter;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements CityRowView, View.OnClickListener {

        private OnItemClickListener onItemClickListener;
        public TextView name, temperature;
        public ImageView icon;

        MyViewHolder(View view, OnItemClickListener listener) {
            super(view);
            name = view.findViewById(R.id.name);
            temperature = view.findViewById(R.id.temperature);
            icon = view.findViewById(R.id.icon);
            onItemClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void setName(String name) {
            this.name.setText(name);
        }

        @Override
        public void setTemperature(double temperature) {
            this.temperature.setText(String.format(Constants.DEGREES_CELSIUS, temperature));
        }

        @Override
        public void setIcon(int iconId) {
            this.icon.setImageResource(iconId);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    public CitiesAdapter(CitiesListContract.Presenter presenter, OnItemClickListener onItemClickListener) {
        this.presenter = presenter;
        this.citiesList = presenter.loadCities();
        this.onItemClickListener = onItemClickListener;
    }

    public void addNewCityToShow(City city){
        citiesList.add(city);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city, parent, false);

        return new MyViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        City city = citiesList.get(position);
        holder.setName(city.getName());
        holder.setTemperature(city.getTemperature());
        holder.setIcon(city.getIconId());
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }
}
