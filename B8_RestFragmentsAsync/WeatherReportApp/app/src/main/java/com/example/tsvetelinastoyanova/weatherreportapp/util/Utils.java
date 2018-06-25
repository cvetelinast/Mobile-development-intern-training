package com.example.tsvetelinastoyanova.weatherreportapp.util;

import android.content.Context;

import com.example.tsvetelinastoyanova.weatherreportapp.R;

public class Utils {
    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }
}
