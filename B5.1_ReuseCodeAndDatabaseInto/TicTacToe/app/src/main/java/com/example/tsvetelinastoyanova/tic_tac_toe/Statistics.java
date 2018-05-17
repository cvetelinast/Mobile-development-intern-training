package com.example.tsvetelinastoyanova.tic_tac_toe;

import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.tsvetelinastoyanova.tic_tac_toe.Database.AppDatabase;
import com.example.tsvetelinastoyanova.tic_tac_toe.Database.Player;

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
        //loadStatisticsFromStorage(); // for wins and loses of single player
        showPlayersAndResult();
        loadPlayersFromDatabase();

    }

    private void loadStatisticsFromStorage() {

        SharedPreferences preferences = getSharedPreferences("WinsNLoses", 0);
        int wins = preferences.getInt(getString(R.string.win), 0);

        TextView textView = findViewById(R.id.wins);
        textView.setText(textView.getText().toString() + wins);

        int loses = preferences.getInt(getString(R.string.lose), 0);
        textView = findViewById(R.id.loses);
        textView.setText(textView.getText().toString() + loses);
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
