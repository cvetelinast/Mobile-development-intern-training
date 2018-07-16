package com.example.tsvetelinastoyanova.weatherapp.util

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.tsvetelinastoyanova.weatherapp.R
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.AppDatabase
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.CitiesLocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.LocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.remote.CitiesRemoteDataSource


object Utils {
    fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }

    fun provideCityRepository(context: Context): CitiesRepository? {
        checkNotNull(context)
        val database = AppDatabase.getInstance(context)

        val localDataSource: CitiesLocalDataSource? = CitiesLocalDataSource.getInstance(AppExecutors(), database.cityDao())
        val remoteDataSource: CitiesRemoteDataSource? = CitiesRemoteDataSource.getInstance()
        localDataSource?.apply {
            remoteDataSource?.let {
                return CitiesRepository.getInstance(this, it)
            }
        }
        return null
    }

    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    fun <T> checkNotNull(reference: T?): T {
        if (reference == null) {
            throw NullPointerException()
        }
        return reference
    }
}