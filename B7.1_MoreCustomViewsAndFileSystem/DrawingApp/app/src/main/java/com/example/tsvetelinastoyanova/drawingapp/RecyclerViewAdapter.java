package com.example.tsvetelinastoyanova.drawingapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Picture> pictures;
   // private boolean isListMode;

    public RecyclerViewAdapter(List<Picture> pictures/*, boolean isListMode*/) {
        this.pictures = pictures;
        /*this.isListMode = isListMode;*/
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View pictureLayoutView;
        if(DrawingList.isListMode) {
            pictureLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_drawing, null);
        } else {
            pictureLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_drawing, null);
        }
       // isListMode = !isListMode;
        ViewHolder viewHolder = new ViewHolder(pictureLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Picture pic = pictures.get(position);
        viewHolder.name.setText(pic.getName());
        viewHolder.lastModified.setText(pic.getLastModified());
        viewHolder.imgViewIcon.setImageResource(pic.getImageUrl());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView lastModified;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            name = itemLayoutView.findViewById(R.id.name);
            lastModified = itemLayoutView.findViewById(R.id.last_modified);
            imgViewIcon = itemLayoutView.findViewById(R.id.drawing);
        }
    }


    @Override
    public int getItemCount() {
        return pictures.size();
    }
}

