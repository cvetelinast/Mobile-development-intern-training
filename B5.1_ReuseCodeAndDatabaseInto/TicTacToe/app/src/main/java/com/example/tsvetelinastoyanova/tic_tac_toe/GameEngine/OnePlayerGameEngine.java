package com.example.tsvetelinastoyanova.tic_tac_toe.GameEngine;

import android.content.SharedPreferences;

import com.example.tsvetelinastoyanova.tic_tac_toe.GameEngine.GameEngine;

public class OnePlayerGameEngine extends GameEngine {
   /* public OnePlayerGameEngine(String firstPlayerName, String secondPlayerName, boolean firstPlayerSymbol) {
        super(firstPlayerName,secondPlayerName, firstPlayerSymbol );
    }

    private void changeStatistics(String s) {
        SharedPreferences preferences = getSharedPreferences("WinsNLoses", 0);
        SharedPreferences.Editor editor = preferences.edit();
        int currentValue = preferences.getInt(s, 0);
        editor.putInt(s, currentValue + 1);
        editor.apply();
    }*/
}
