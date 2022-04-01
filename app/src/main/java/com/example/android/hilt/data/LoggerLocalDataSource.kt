/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hilt.data

import android.os.Handler
import android.os.Looper
import com.example.android.hilt.ui.ButtonsFragment
import com.example.android.hilt.ui.LogsFragment
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Data manager class that handles data manipulation between the database and the UI. The Inject
 * annotation Identifies injectable constructors, methods, and fields. Constructors are injected
 * first, followed by fields, and then methods. Fields and methods in superclasses are injected
 * before those in subclasses. Ordering of injection among fields and among methods in the same
 * class is not specified. Injectable constructors are annotated with Inject and accept zero or
 * more dependencies as arguments. Inject can apply to at most one constructor per class. Hilt
 * generates the [LoggerLocalDataSource_Factory] class from this file which implements the
 * `Factory` of [LoggerLocalDataSource] interface with its [LoggerLocalDataSource_Factory.get]
 * method supplying a new instance of [LoggerLocalDataSource] constructed from a fully-constructed
 * and injected instance of [LogDao]. Used as the argument of the `bindDatabaseLogger` method
 * of the `LoggingDatabaseModule` abstract class used to generate the [LoggerDataSource] it injects
 * into [ButtonsFragment] and [LogsFragment] when the `DatabaseLogger` annotation is applied instead
 * of the `InMemoryLogger` annotation to the `Inject` annotation of the [LoggerDataSource].
 *
 * @param logDao the [LogDao] we are to use to access our database.
 */
class LoggerLocalDataSource @Inject constructor(private val logDao: LogDao) : LoggerDataSource {

    /**
     * The [ExecutorService] that we use as the [Executor] when we call methods in our [LogDao]
     */
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)

    /**
     * The [Handler] we use to post [Runnable]'s to be run on the main thread.
     */
    private val mainThreadHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    /**
     * Constructs a [Log] instance from our [String] parameter [msg] and the current system time and
     * adds it to our Room database.
     *
     * @param msg the [String] to use for the [Log] instance we construct and add to our Room
     * database.
     */
    override fun addLog(msg: String) {
        executorService.execute {
            logDao.insertAll(
                Log(
                    msg,
                    System.currentTimeMillis()
                )
            )
        }
    }

    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        executorService.execute {
            val logs = logDao.getAll()
            mainThreadHandler.post { callback(logs) }
        }
    }

    override fun removeLogs() {
        executorService.execute {
            logDao.nukeTable()
        }
    }
}
