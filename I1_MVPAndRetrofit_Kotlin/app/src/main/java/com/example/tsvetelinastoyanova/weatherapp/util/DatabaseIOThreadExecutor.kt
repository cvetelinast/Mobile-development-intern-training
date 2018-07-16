package com.example.tsvetelinastoyanova.weatherapp.util

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DatabaseIOThreadExecutor : Executor {

    private val mDiskIO: Executor

    init {
        mDiskIO = Executors.newSingleThreadExecutor()
    }

    override fun execute(command: Runnable) {
        mDiskIO.execute(command)
    }
}
