package com.example.tsvetelinastoyanova.b3_listsintro;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Planet> planetList;
    private RecyclerViewClickListener recyclerViewClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public ImageView picture;

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.onClick(view, getAdapterPosition());
        }

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            name = view.findViewById(R.id.planet_name);
            picture = view.findViewById(R.id.planet_picture);
            recyclerViewClickListener = listener;
            view.setOnClickListener(this);
        }
    }

    public MyAdapter(List<Planet> planetList, RecyclerViewClickListener listener) {
        this.planetList = planetList;
        this.recyclerViewClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.planets, parent, false);
        return new MyViewHolder(itemView, recyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Planet planet = planetList.get(position);
        Drawable drawable = holder.itemView.getContext().getResources().getDrawable(planet.getPictureId());
        holder.picture.setImageDrawable(drawable);
        holder.name.setText(planet.getName());
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }
}
