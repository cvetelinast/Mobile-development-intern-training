package com.example.tsvetelinastoyanova.tic_tac_toe.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "players")
public class Player {

    @PrimaryKey
    @NonNull
    private String name;

    @ColumnInfo(name = "wins")
    private int wins;

    @ColumnInfo(name = "loses")
    private int loses;

    public Player(@NonNull String name, int wins, int loses) {
        this.name = name;
        this.wins = wins;
        this.loses = loses;
    }

    public @NonNull
    String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }
}
