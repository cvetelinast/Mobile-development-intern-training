package com.example.tsvetelinastoyanova.weatherapp.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "cities")

class CityEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "cityId")
    var cityId: Int = 0

    @ColumnInfo(name = "lastTemperature")
    var lastTemperature: Double = 0.toDouble()

    @ColumnInfo(name = "lastImageId")
    var lastImageId: Int = 0
}
