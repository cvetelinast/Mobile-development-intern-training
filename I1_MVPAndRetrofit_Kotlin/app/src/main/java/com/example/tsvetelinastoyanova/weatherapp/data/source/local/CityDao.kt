package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity

@Dao
interface CityDao {

    @get:Query("SELECT * FROM cities")
    val all: List<CityEntity>

    @Insert
    fun insertCity(city: CityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCities(vararg cities: CityEntity)

    @Query("UPDATE cities SET lastTemperature = :lastTemperature, lastImageId = :lastImageId WHERE name = :name")
    fun updateCity(name: String, lastTemperature: Double, lastImageId: Int)

    @Query("SELECT name FROM cities WHERE name = :name LIMIT 1")
    fun getCityName(name: String): String

    @Query("SELECT * FROM cities WHERE name = :name LIMIT 1")
    fun getCity(name: String): CityEntity

    @Query("DELETE FROM cities")
    fun deleteAll()

    @Query("DELETE FROM cities WHERE name = :name")
    fun deleteCity(name: String)
}
