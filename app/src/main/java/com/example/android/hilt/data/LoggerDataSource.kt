package com.example.android.hilt.data

/**
 * Common interface for Logger data sources.
 */
interface LoggerDataSource {
    /**
     * TODO: Add kdoc
     */
    fun addLog(msg: String)

    /**
     * TODO: Add kdoc
     */
    fun getAllLogs(callback: (List<Log>) -> Unit)

    /**
     * TODO: Add kdoc
     */
    fun removeLogs()
}
