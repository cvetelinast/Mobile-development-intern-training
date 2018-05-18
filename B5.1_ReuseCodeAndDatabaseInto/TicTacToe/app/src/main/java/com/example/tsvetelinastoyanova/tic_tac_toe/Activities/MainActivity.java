package com.example.tsvetelinastoyanova.tic_tac_toe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.tic_tac_toe.R;

public class MainActivity extends AppCompatActivity {

    String firstPlayerName;
    String secondPlayerName;
    private boolean isGameForTwoPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckBox checkBox = findViewById(R.id.checkbox_two_players);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            onCheckBoxClicked(isChecked);
        });
    }

    public void startNewGame(View view) {
        initFieldsWithNames();
        if (areNamesValid()) {
            Intent startNewGameIntent = makeIntent();
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

    private Intent makeIntent() {
        Intent startNewGameIntent = new Intent(this, GameActivity.class);
        startNewGameIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startNewGameIntent.putExtra(getString(R.string.two_players_game), isGameForTwoPlayers);
        startNewGameIntent.putExtra(getString(R.string.first_player_name), firstPlayerName);
        if (isGameForTwoPlayers) {
            startNewGameIntent.putExtra(getString(R.string.second_player_name), secondPlayerName);
        } else {
            startNewGameIntent.putExtra(getString(R.string.second_player_name), getString(R.string.computer));
        }
        return startNewGameIntent;
    }

    private void initFieldsWithNames() {
        TextView viewFirstPlayerName = findViewById(R.id.first_player_name);
        firstPlayerName = viewFirstPlayerName.getText().toString();

        TextView viewSecondPlayerName = findViewById(R.id.second_player_name);
        secondPlayerName = viewSecondPlayerName.getText().toString();
    }

    private boolean areNamesValid() {
        if (firstPlayerName.equals(secondPlayerName) || firstPlayerName.length() == 0) {
            return false;
        } else if (secondPlayerName.length() == 0 && isGameForTwoPlayers) {
            return false;
        }
        return true;
    }

    private void onCheckBoxClicked(boolean isChecked) {
        final EditText editText = findViewById(R.id.second_player_name);
        if (isChecked) {
            editText.setVisibility(View.VISIBLE);
            isGameForTwoPlayers = true;
        } else {
            editText.setVisibility(View.INVISIBLE);
            isGameForTwoPlayers = false;
        }
    }
}
