package com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.visualization;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.weatherreportrevisited.City;
import com.example.tsvetelinastoyanova.weatherreportrevisited.Constants;
import com.example.tsvetelinastoyanova.weatherreportrevisited.R;
import com.example.tsvetelinastoyanova.weatherreportrevisited.citieslist.CitiesListContract;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder> {

    private final List<City> citiesList = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private final CitiesListContract.Presenter presenter;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements CityRowView, View.OnClickListener {

        private OnItemClickListener onItemClickListener;
        public TextView name, temperature;
        public ImageView icon;
        public RelativeLayout viewBackground, viewForeground;

        MyViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            name = view.findViewById(R.id.name);
            temperature = view.findViewById(R.id.temperature);
            icon = view.findViewById(R.id.icon);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            this.onItemClickListener = onItemClickListener;
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
        this.onItemClickListener = onItemClickListener;
    }

    public List<City> getCitiesList() {
        return citiesList;
    }

    public void addNewCityToShow(City city) {
        if(!citiesList.contains(city)) {
            citiesList.add(city);
            notifyDataSetChanged();
        }
    }

    public String getCityNameOnIndex(int index) {
        return citiesList.get(index).getName();
    }

    public City getCityOnIndex(int index) {
        return citiesList.get(index);
    }

    public void removeCity(int position) {
        citiesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreCity(City city, int position) {
        citiesList.add(position, city);
        notifyItemInserted(position);
    }

    public void refreshCity(City newCity) {
        for (int i = 0; i < getCitiesList().size(); i++) {
            City c = getCitiesList().get(i);
            if (c.getName().equals(newCity.getName())) {
//                Log.d("tag","REFRESH: city: " + c.getName()+" from "+ c.getTemperature() + " to " + newCity.getTemperature() + " item number "+ i);
                getCitiesList().set(i,newCity);
                notifyItemChanged(i);
                return;
            }
        }
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
