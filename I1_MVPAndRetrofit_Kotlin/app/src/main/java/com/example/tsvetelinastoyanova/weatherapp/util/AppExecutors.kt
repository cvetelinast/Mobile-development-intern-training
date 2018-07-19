package com.example.tsvetelinastoyanova.weatherapp.util

import android.os.Handler
import android.os.Looper
import android.support.annotation.VisibleForTesting
import java.util.concurrent.Executor
import java.util.concurrent.Executors
/*

class AppExecutors @VisibleForTesting
internal constructor(private val databaseIO: Executor, private val networkIO: Executor, private val mainThread: Executor) {

    constructor() : this(DatabaseIOThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT),
            MainThreadExecutor())

    fun databaseIO(): Executor {
        return databaseIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {

        private val THREAD_COUNT = 3
    }
}*/