package com.example.tsvetelinastoyanova.tic_tac_toe.heplerclasses;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.tsvetelinastoyanova.tic_tac_toe.database.AppDatabase;
import com.example.tsvetelinastoyanova.tic_tac_toe.database.Player;

public class DatabaseConnector {

    private String winner;
    private String loser;

    public DatabaseConnector(String winner, String loser) {
        this.winner = winner;
        this.loser = loser;
    }

    public void databaseManipulation(Context context) {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "players").build();
            if (db.userDao().getUserWithThisName(winner) == 0) {
                db.userDao().insertPlayer(new Player(winner, 0, 0));
            }
            db.userDao().incrementNumWinsOfPlayer(winner);

            if (db.userDao().getUserWithThisName(loser) == 0) {
                db.userDao().insertPlayer(new Player(loser, 0, 0));
            }
            db.userDao().decrementNumLosesOfPlayer(loser);
        }
        ).start();
    }
}
