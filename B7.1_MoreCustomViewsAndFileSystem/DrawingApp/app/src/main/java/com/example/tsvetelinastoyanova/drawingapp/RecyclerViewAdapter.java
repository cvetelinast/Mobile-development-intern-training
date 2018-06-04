package com.example.tsvetelinastoyanova.drawingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Picture> pictures;
    private Context context; // we want to show popup menu for single row

    public RecyclerViewAdapter(List<Picture> pictures, Context context) {
        this.pictures = pictures;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View pictureLayoutView;
        if (DrawingList.isListMode) {
            pictureLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_drawing, null);
        } else {
            pictureLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_drawing, null);
        }
        return new ViewHolder(pictureLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Picture pic = pictures.get(position);
        viewHolder.name.setText(pic.getName());
        viewHolder.lastModified.setText(pic.getLastModified());
        viewHolder.imgViewIcon.setImageResource(pic.getImageUrl());
        viewHolder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "show menu");
                createMenuForView(viewHolder);
            }
        });
    }

    private void createMenuForView(ViewHolder viewHolder) {
        PopupMenu popup = new PopupMenu(context, viewHolder.buttonViewOption);
        popup.inflate(R.menu.options_menu_drawings);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        Log.d("tag", "delete");
                        break;
                    case R.id.rename:
                        Log.d("tag", "rename");
                        break;
                    case R.id.share:
                        Log.d("tag", "share");
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView lastModified;
        public ImageView imgViewIcon;
        public TextView buttonViewOption;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            name = itemLayoutView.findViewById(R.id.name);
            lastModified = itemLayoutView.findViewById(R.id.last_modified);
            imgViewIcon = itemLayoutView.findViewById(R.id.drawing);
            buttonViewOption = itemView.findViewById(R.id.view_options);
        }
    }


    @Override
    public int getItemCount() {
        return pictures.size();
    }
}

