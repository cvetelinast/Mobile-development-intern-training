package com.example.tsvetelinastoyanova.weatherreportapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.tsvetelinastoyanova.weatherreportapp.City;

@Database(entities = {CityEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CityDao cityDao();
}
