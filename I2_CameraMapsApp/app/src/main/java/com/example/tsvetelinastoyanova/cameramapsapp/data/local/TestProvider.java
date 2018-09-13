package com.example.tsvetelinastoyanova.cameramapsapp.data.local;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestProvider implements Provider {
    @Override
    public Scheduler provideUiThread() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler provideBackgroudThread() {
        return Schedulers.trampoline();
    }
}