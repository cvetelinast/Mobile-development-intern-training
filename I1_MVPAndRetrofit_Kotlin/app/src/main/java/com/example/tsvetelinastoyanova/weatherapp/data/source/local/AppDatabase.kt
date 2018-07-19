package com.example.tsvetelinastoyanova.weatherapp.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.tsvetelinastoyanova.weatherapp.Constants
import com.example.tsvetelinastoyanova.weatherapp.data.CityEntity

@Database(entities = [CityEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context)
                            .also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, Constants.DATABASE_NAME)
                        .build()
    }
}