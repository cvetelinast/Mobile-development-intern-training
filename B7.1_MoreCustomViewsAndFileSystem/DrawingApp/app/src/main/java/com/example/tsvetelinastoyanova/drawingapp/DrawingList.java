package com.example.tsvetelinastoyanova.drawingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DrawingList extends AppCompatActivity {

    static boolean isListMode = true;

    private RecyclerView drawingsRecyclerView;
    private RecyclerViewAdapter adapter;
    private Menu menu;
    private LinearLayoutManager gridLayoutManager;
    private LinearLayoutManager horizontalLayoutManager;
    private List<Picture> pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initComponentsOfRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.appbar, menu);
        if (isListMode) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_view_module_black_24dp));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_view_list_black_24dp));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grid_list_switch:
                if (isListMode) {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_view_list_black_24dp));
                    drawingsRecyclerView.setLayoutManager(gridLayoutManager);
                } else {
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_view_module_black_24dp));
                    drawingsRecyclerView.setLayoutManager(horizontalLayoutManager);
                }
                drawingsRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                isListMode = !isListMode;
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isListMode", isListMode);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        isListMode = savedInstanceState.getBoolean("isListMode");
    }

    public List<Picture> fill_with_data() {

        List<Picture> data = new ArrayList<>();

        data.add(new Picture("Name 1", "Date 1", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 2", "Date 2", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 3", "Date 3", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 4", "Date 4", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 5", "Date 5", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 6", "Date 6", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 7", "Date 7", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 8", "Date 8", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 9", "Date 9", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 10", "Date 10", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 11", "Date 11", R.drawable.ic_launcher_background));
        data.add(new Picture("Name 12", "Date 12", R.drawable.ic_launcher_background));
        return data;
    }

    private void initView() {
        setContentView(R.layout.activity_drawing_list);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initComponentsOfRecyclerView() {
        drawingsRecyclerView = findViewById(R.id.drawings_recycler_view);
        pictures = fill_with_data();
        adapter = new RecyclerViewAdapter(pictures);

        gridLayoutManager = new GridLayoutManager(DrawingList.this, 2);
        horizontalLayoutManager = new LinearLayoutManager(DrawingList.this, LinearLayoutManager.VERTICAL, false);
        setLayoutManager();
        drawingsRecyclerView.setAdapter(adapter);
    }

    private void setLayoutManager() {
        if (isListMode) {
            drawingsRecyclerView.setLayoutManager(horizontalLayoutManager);
        } else {
            drawingsRecyclerView.setLayoutManager(gridLayoutManager);
        }
    }
}
