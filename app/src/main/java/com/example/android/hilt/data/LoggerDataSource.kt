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
