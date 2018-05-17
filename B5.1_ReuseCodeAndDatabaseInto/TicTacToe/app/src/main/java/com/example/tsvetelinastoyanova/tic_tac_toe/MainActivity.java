package com.example.tsvetelinastoyanova.tic_tac_toe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String firstPlayerName;
    String secondPlayerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startNewGame(View view) {
        if (validateNamesAndSetFieldsWithThem()) {
            Intent startNewGameIntent = new Intent(this, GameActivity.class);
            startNewGameIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startNewGameIntent.putExtra(getString(R.string.first_player_name), firstPlayerName);
            startNewGameIntent.putExtra(getString(R.string.second_player_name), secondPlayerName);
            startActivity(startNewGameIntent);
        } else {
            Toast toast = Toast.makeText(this, getString(R.string.wrong_names_message), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void openStatistics(View view) {
        Intent openStatisticsIntent = new Intent(this, Statistics.class);
        startActivity(openStatisticsIntent);
    }

    public boolean validateNamesAndSetFieldsWithThem() {
        TextView viewFirstPlayerName = findViewById(R.id.first_player_name);
        firstPlayerName = viewFirstPlayerName.getText().toString();

        TextView viewSecondPlayerName = findViewById(R.id.second_player_name);
        secondPlayerName = viewSecondPlayerName.getText().toString();

        if (firstPlayerName.equals(secondPlayerName) || firstPlayerName.length() == 0 || secondPlayerName.length() == 0) {
            return false;
        }
        return true;
    }
}
