package com.example.tsvetelinastoyanova.weatherreportapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CityDao {

    @Query("SELECT * FROM cities")
    List<CityEntity> getAll();

    @Insert
    void insertCity(CityEntity city);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCities(CityEntity... cities);

    @Query("UPDATE cities SET lastTemperature = :lastTemperature, lastImageId = :lastImageId WHERE name = :name")
    public void updateCity(String name, double lastTemperature, int lastImageId);

}
