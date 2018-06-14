package com.example.tsvetelinastoyanova.weatherreportapp;

import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

   // public final LiveData<User> userLiveData = new LiveData<>();

    public SharedViewModel() {
        // trigger user load.
    }

    void doAction() {
        // depending on the action, do necessary business logic calls and update the
        // userLiveData.
    }
}
