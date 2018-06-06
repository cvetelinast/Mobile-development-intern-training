package com.example.tsvetelinastoyanova.drawingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.drawingapp.activities.DrawingList;
import com.example.tsvetelinastoyanova.drawingapp.handlers.PopupMenuHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context; // we want to show popup menu for single row
    private final int imageWidthPixels = 500;
    private final int imageHeightPixels = 500;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    List<File> files;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        String nameOfDirectory = context.getResources().getString(R.string.directory);
        String folder = context.getFilesDir().getAbsolutePath() + File.separator + nameOfDirectory;
        createDirectoryOfDrawingsIfNotExist(folder);
        File directoryFiles = new File(folder);
        files = new ArrayList<>();
        for (File file : directoryFiles.listFiles()) {
            files.add(file);
        }
    }

    void createDirectoryOfDrawingsIfNotExist(String folder){
        File subFolder = new File(folder);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        File file = files.get(position);
        GlideApp.with(context)
                .load(file)
                .override(imageWidthPixels, imageHeightPixels)
                .into(viewHolder.imgViewIcon);

        String name = context.getResources().getString(R.string.name, file.getName());
        viewHolder.name.setText(name);
        viewHolder.lastModified.setText(context.getResources().getString(R.string.last_modified, dateFormat.format(file.lastModified()).toString()));
        viewHolder.buttonViewOption.setOnClickListener((v) -> {
            {
                Log.d("tag", "show menu");
                createMenuForView(position, viewHolder);
            }
        });
    }

    private void createMenuForView(int position, ViewHolder viewHolder) {
        PopupMenu popup = new PopupMenu(context, viewHolder.buttonViewOption);
        popup.inflate(R.menu.options_menu_drawings);
        PopupMenuHandler popupMenuHandler = new PopupMenuHandler(context, popup, position, files, this);
        popupMenuHandler.setClickListenersOnPopupMenu();
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
        return files.size();
    }
}

