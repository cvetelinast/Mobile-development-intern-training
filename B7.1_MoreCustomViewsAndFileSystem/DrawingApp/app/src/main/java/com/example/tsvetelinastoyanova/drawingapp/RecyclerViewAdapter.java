package com.example.tsvetelinastoyanova.drawingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.signature.ObjectKey;
import com.example.tsvetelinastoyanova.drawingapp.activities.DrawingList;
import com.example.tsvetelinastoyanova.drawingapp.activities.DrawingScreen;
import com.example.tsvetelinastoyanova.drawingapp.handlers.FilesHandler;
import com.example.tsvetelinastoyanova.drawingapp.handlers.PopupMenuHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final int IMAGE_WIDTH_PIXELS = 500;
    private final int IMAGE_HEIGHT_PIXELS = 500;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private static RecyclerViewClickListener recyclerViewClickListener;
    private Context context; // we want to show popup menu for single row
    private Activity activity;
    private List<File> files;

    public RecyclerViewAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity;
        recyclerViewClickListener = initRecyclerViewClickListener();
        initAndFillListWithFiles();
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
        return new ViewHolder(pictureLayoutView, recyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        File file = files.get(position);
        GlideApp.with(context)
                .load(file)
                .signature(new ObjectKey(file.getName() + file.lastModified()))
                .override(IMAGE_WIDTH_PIXELS, IMAGE_HEIGHT_PIXELS)
                .into(viewHolder.imgViewIcon);
        initViewHolderAttributes(viewHolder, position, file);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    private void initAndFillListWithFiles() {
        String nameOfDirectory = context.getResources().getString(R.string.directory);
        String folder = context.getFilesDir().getAbsolutePath() + File.separator + nameOfDirectory;
        FilesHandler filesHandler = new FilesHandler();
        File directoryFiles = filesHandler.prepareDirectory(folder);
        files = new ArrayList<>();
        files.addAll(Arrays.asList(directoryFiles.listFiles()));
    }

    private RecyclerViewClickListener initRecyclerViewClickListener() {
        RecyclerViewClickListener listenerEditDrawing = (view, position) -> {
            final Intent intent = new Intent(context, DrawingScreen.class);
            intent.putExtra(context.getResources().getString(R.string.edit_drawing), files.get(position).getAbsolutePath());
            context.startActivity(intent);
        };
        return listenerEditDrawing;
    }

    private void initViewHolderAttributes(final ViewHolder viewHolder, int position, File file) {
        String name = context.getResources().getString(R.string.name, file.getName());
        viewHolder.name.setText(name);
        viewHolder.lastModified.setText(context.getResources().getString(R.string.last_modified, dateFormat.format(file.lastModified()).toString()));
        viewHolder.buttonViewOption.setOnClickListener((v) -> createMenuForView(position, viewHolder));
    }

    private void createMenuForView(int position, ViewHolder viewHolder) {
        PopupMenu popup = new PopupMenu(context, viewHolder.buttonViewOption);
        popup.inflate(R.menu.options_menu_drawings);
        PopupMenuHandler popupMenuHandler = new PopupMenuHandler(context, popup, position, files, this);
        popupMenuHandler.setClickListenersOnPopupMenu(activity);
        popup.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        TextView lastModified;
        ImageView imgViewIcon;
        TextView buttonViewOption;

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.onClick(view, getAdapterPosition());
        }

        ViewHolder(View itemLayoutView, RecyclerViewClickListener listener) {
            super(itemLayoutView);
            name = itemLayoutView.findViewById(R.id.name);
            lastModified = itemLayoutView.findViewById(R.id.last_modified);
            imgViewIcon = itemLayoutView.findViewById(R.id.drawing);
            buttonViewOption = itemView.findViewById(R.id.view_options);
            initClickListener(itemLayoutView, listener);
        }

        private void initClickListener(View itemLayoutView, RecyclerViewClickListener listener) {
            recyclerViewClickListener = listener;
            itemLayoutView.setOnClickListener(this);
        }
    }
}

