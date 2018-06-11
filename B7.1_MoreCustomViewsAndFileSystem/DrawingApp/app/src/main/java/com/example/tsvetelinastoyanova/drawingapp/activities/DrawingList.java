package com.example.tsvetelinastoyanova.drawingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.FloatingActionButton;

import com.example.tsvetelinastoyanova.drawingapp.R;
import com.example.tsvetelinastoyanova.drawingapp.RecyclerViewAdapter;

public class DrawingList extends AppCompatActivity {

    public static boolean isListMode = true;

    private RecyclerView drawingsRecyclerView;
    private RecyclerViewAdapter adapter;
    private Menu menu;
    private LinearLayoutManager gridLayoutManager;
    private LinearLayoutManager horizontalLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setOnClickListenerToFloatingButton();
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
                isListMode = !isListMode;
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void initView() {
        setContentView(R.layout.activity_drawing_list);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initComponentsOfRecyclerView() {
        drawingsRecyclerView = findViewById(R.id.drawings_recycler_view);
        adapter = new RecyclerViewAdapter(this/*, initRecyclerViewClickListener()*/);

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

    private void setOnClickListenerToFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener((view) -> {
            final Intent intent = new Intent(this, DrawingScreen.class);
            startActivity(intent);
        });
    }
}
