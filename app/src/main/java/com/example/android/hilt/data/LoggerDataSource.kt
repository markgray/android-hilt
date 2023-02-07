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

import java.util.LinkedList

/**
 * Common interface for Logger data sources.
 */
interface LoggerDataSource {
    /**
     * Constructs a [Log] instance from its [String] parameter [msg] and the current system time and
     * adds it to the Logger data source, either to the in memory [LinkedList] of [Log] that is used
     * by [LoggerInMemoryDataSource] or to the Room database used by [LoggerLocalDataSource].
     *
     * @param msg the [String] to use for the [Log] instance we construct and add to the Logger data
     * source.
     */
    fun addLog(msg: String)

    /**
     * Retrieves all of the [Log] entries stored by our Logger data source as a [List] and feeds it
     * as the argument of the [callback] lambda we are called with.
     *
     * @param callback a lambda which takes a [List] of [Log] instances as its argument.
     */
    fun getAllLogs(callback: (List<Log>) -> Unit)

    /**
     * Removes all [Log] instances stored by our Logger data source.
      */
    fun removeLogs()
}
