package com.example.tsvetelinastoyanova.weatherreportapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CityDao {

    @Query("SELECT * FROM cities")
    List<CityEntity> getAll();

    @Insert
    void insertCity(CityEntity city);

}
