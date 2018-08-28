package com.example.tsvetelinastoyanova.hackernewsapp.common.schedulers

import io.reactivex.schedulers.Schedulers
import io.reactivex.Scheduler

class ImmediateSchedulerProvider : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}