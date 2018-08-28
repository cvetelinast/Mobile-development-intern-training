package com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider : BaseSchedulerProvider {

    companion object {
        @Volatile
        private var INSTANCE: SchedulerProvider? = null

        @Synchronized
        fun getInstance(): SchedulerProvider {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SchedulerProvider().also { INSTANCE = it }
            }
        }
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}