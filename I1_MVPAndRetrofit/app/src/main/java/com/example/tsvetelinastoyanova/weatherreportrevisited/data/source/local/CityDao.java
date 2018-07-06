package com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.tsvetelinastoyanova.weatherreportrevisited.data.CityEntity;

import java.util.List;

@Dao
public interface CityDao {

    @Query("SELECT * FROM cities")
    List<CityEntity> getAll();

    @Insert
    void insertCity(CityEntity city);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCities(CityEntity... cities);

    @Query("UPDATE cities SET lastTemperature = :lastTemperature, lastImageId = :lastImageId WHERE name = :name")
    void updateCity(String name, double lastTemperature, int lastImageId);

    @Query("SELECT name FROM cities WHERE name = :name LIMIT 1")
    String getCityName(String name);

    @Query("SELECT * FROM cities WHERE name = :name LIMIT 1")
    CityEntity getCity(String name);

    @Query("DELETE FROM cities")
    void deleteAll();

    @Query("DELETE FROM cities WHERE name = :name")
    void deleteCity(String name);
}
