package com.example.tsvetelinastoyanova.weatherreportrevisited.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.tsvetelinastoyanova.weatherreportrevisited.R;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.CitiesRepository;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local.AppDatabase;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.local.CitiesLocalDataSource;
import com.example.tsvetelinastoyanova.weatherreportrevisited.data.source.remote.CitiesRemoteDataSource;

public class Utils {
    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static CitiesRepository provideCityRepository(@NonNull Context context) {
        checkNotNull(context);
        AppDatabase database = AppDatabase.getInstance(context);

        return CitiesRepository.getInstance(
                CitiesLocalDataSource.getInstance(new AppExecutors(),
                database.cityDao()),
                CitiesRemoteDataSource.getInstance());

    }
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
