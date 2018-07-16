package com.example.tsvetelinastoyanova.weatherapp.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.tsvetelinastoyanova.weatherapp.Constants;
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity;

@Database(entities = {CityEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CityDao cityDao();

    private static AppDatabase INSTANCE;

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, Constants.Companion.getDATABASE_NAME())
                        .build();
            }
            return INSTANCE;
        }
    }
}