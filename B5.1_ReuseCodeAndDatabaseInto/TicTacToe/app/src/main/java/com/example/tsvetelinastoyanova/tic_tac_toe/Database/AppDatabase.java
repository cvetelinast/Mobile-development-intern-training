package com.example.tsvetelinastoyanova.tic_tac_toe.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Player.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlayerDao userDao();
}