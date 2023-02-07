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

import com.example.android.hilt.ui.ButtonsFragment
import com.example.android.hilt.ui.LogsFragment
import java.util.LinkedList
import javax.inject.Inject

/**
 * In memory implementaton of the [LoggerDataSource] Logger data source interface.  The Inject
 * annotation Identifies injectable constructors, methods, and fields. Constructors are injected
 * first, followed by fields, and then methods. Fields and methods in superclasses are injected
 * before those in subclasses. Ordering of injection among fields and among methods in the same
 * class is not specified. Injectable constructors are annotated with Inject and accept zero or
 * more dependencies as arguments. Inject can apply to at most one constructor per class. Hilt
 * generates the [LoggerInMemoryDataSource_Factory]  class from this file which implements the
 * `Factory` of [LoggerInMemoryDataSource] with its [LoggerInMemoryDataSource_Factory.get] method
 * supplying a new instance of [LoggerInMemoryDataSource]. Used as the argument of the
 * `bindDatabaseLogger` method of the `LoggingDatabaseModule` abstract class used to generate the
 * [LoggerDataSource] it injects into [ButtonsFragment] and [LogsFragment] when the `InMemoryLogger`
 * annotation is applied instead of the `DatabaseLogger` annotation to the `Inject` annotation of
 * the [LoggerDataSource].
 */
class LoggerInMemoryDataSource @Inject constructor() : LoggerDataSource {

    /**
     * Our "database" of [Log] entries.
     */
    private val logs = LinkedList<Log>()

    /**
     * Constructs a [Log] instance from its [String] parameter [msg] and the current system time and
     * adds it to our [LinkedList] of [Log] field [logs].
     *
     * @param msg the [String] to use for the [Log] instance we construct and add to the Logger data
     * source.
     */
    override fun addLog(msg: String) {
        logs.addFirst(Log(msg, System.currentTimeMillis()))
    }

    /**
     * Retrieves all of the [Log] entries stored in our [LinkedList] of [Log] field [logs] and feeds
     * it as the argument of the [callback] lambda we are called with.
     *
     * @param callback a lambda which takes a [List] of [Log] instances as its argument.
     */
    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        callback(logs)
    }

    /**
     * Removes all [Log] instances stored in our [LinkedList] of [Log] field [logs].
     */
    override fun removeLogs() {
        logs.clear()
    }
}
