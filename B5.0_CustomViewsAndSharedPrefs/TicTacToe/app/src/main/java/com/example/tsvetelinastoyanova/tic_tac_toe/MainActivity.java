package com.example.tsvetelinastoyanova.tic_tac_toe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startNewGame(View view){
        Intent startNewGameIntent = new Intent(this, GameActivity.class);
        startNewGameIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(startNewGameIntent);
    }

    public void openStatistics(View view){
        Intent openStatisticsIntent = new Intent(this, Statistics.class);
        startActivity(openStatisticsIntent);
    }
}
