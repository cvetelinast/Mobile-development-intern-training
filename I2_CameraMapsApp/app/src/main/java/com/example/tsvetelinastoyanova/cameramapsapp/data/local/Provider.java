package com.example.tsvetelinastoyanova.cameramapsapp.data.local;

import io.reactivex.Scheduler;

public interface Provider {
    Scheduler provideUiThread();

    Scheduler provideBackgroudThread();

}
