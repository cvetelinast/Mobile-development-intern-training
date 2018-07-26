package com.example.tsvetelinastoyanova.weatherapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.tsvetelinastoyanova.weatherapp.R
import com.example.tsvetelinastoyanova.weatherapp.data.source.CitiesRepository
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.AppDatabase
import com.example.tsvetelinastoyanova.weatherapp.data.source.local.CitiesLocalDataSource
import com.example.tsvetelinastoyanova.weatherapp.data.source.remote.CitiesRemoteDataSource

object Utils {
    fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }

    fun provideCityRepository(context: Context): CitiesRepository {
        checkNotNull(context)
        val database = AppDatabase.getInstance(context)

        val localDataSource: CitiesLocalDataSource = CitiesLocalDataSource.getInstance(database.cityDao())
        val remoteDataSource: CitiesRemoteDataSource = CitiesRemoteDataSource.getInstance()

        return CitiesRepository.getInstance(localDataSource, remoteDataSource)
    }

    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    fun removeFragment(fragmentManager: FragmentManager,
                       fragment: Fragment) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commit()
    }

    fun <T> checkNotNull(reference: T?): T {
        if (reference == null) {
            throw NullPointerException()
        }
        return reference
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}