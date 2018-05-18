package com.example.tsvetelinastoyanova.tic_tac_toe.activities;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tsvetelinastoyanova.tic_tac_toe.database.AppDatabase;
import com.example.tsvetelinastoyanova.tic_tac_toe.database.Player;
import com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses.PlayersAdapter;
import com.example.tsvetelinastoyanova.tic_tac_toe.R;

import java.util.ArrayList;
import java.util.List;


public class Statistics extends AppCompatActivity {
    private List<Player> playersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        showPlayersAndResult();
        loadPlayersFromDatabase();
    }

    private void loadPlayersFromDatabase() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "players").build();
            playersList.addAll(db.userDao().getAllOrdered());
            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void showPlayersAndResult() {
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new PlayersAdapter(playersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
