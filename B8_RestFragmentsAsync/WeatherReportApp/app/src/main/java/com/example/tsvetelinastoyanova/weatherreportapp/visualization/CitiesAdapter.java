package com.example.tsvetelinastoyanova.weatherreportapp.visualization;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.weatherreportapp.City;
import com.example.tsvetelinastoyanova.weatherreportapp.Constants;
import com.example.tsvetelinastoyanova.weatherreportapp.R;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder> {

    private List<City> citiesList;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, temperature;
        public ImageView icon;

        public MyViewHolder(View view, OnItemClickListener listener) {
            super(view);
            name = view.findViewById(R.id.name);
            temperature = view.findViewById(R.id.temperature);
            icon = view.findViewById(R.id.icon);
            onItemClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    public CitiesAdapter(List<City> citiesList, OnItemClickListener onItemClickListener) {
        this.citiesList = citiesList;
        this.onItemClickListener = onItemClickListener;
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
        holder.name.setText(city.getName());
        holder.temperature.setText(String.format(Constants.DEGREES_CELSIUS, city.getTemperature()));
        holder.icon.setImageResource(city.getIconId());
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }
}
