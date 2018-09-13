package com.example.tsvetelinastoyanova.cameramapsapp.data.local;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider implements Provider {
    @Override
    public Scheduler provideUiThread() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler provideBackgroudThread() {
        return Schedulers.io();
    }
}
