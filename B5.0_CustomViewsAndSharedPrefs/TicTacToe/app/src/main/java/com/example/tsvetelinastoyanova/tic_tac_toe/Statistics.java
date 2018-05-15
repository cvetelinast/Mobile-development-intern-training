package com.example.tsvetelinastoyanova.tic_tac_toe;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        loadStatisticsFromStorage();
    }

    private void loadStatisticsFromStorage(){

        SharedPreferences preferences = getSharedPreferences("WinsNLoses",0);
        int wins = preferences.getInt(getString(R.string.win), 0);

        TextView textView = findViewById(R.id.wins);
        textView.setText(textView.getText().toString() + wins);

        int loses = preferences.getInt(getString(R.string.lose), 0);
        textView = findViewById(R.id.loses);
        textView.setText(textView.getText().toString() + loses);
    }
}
