package com.example.android.hilt.data

import dagger.hilt.android.scopes.ActivityScoped
import java.util.LinkedList
import javax.inject.Inject

/**
 * TODO: Add kdoc
 */
class LoggerInMemoryDataSource @Inject constructor() : LoggerDataSource {

    private val logs = LinkedList<Log>()

    /**
     * TODO: Add kdoc
     */
    override fun addLog(msg: String) {
        logs.addFirst(Log(msg, System.currentTimeMillis()))
    }

    /**
     * TODO: Add kdoc
     */
    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        callback(logs)
    }

    /**
     * TODO: Add kdoc
     */
    override fun removeLogs() {
        logs.clear()
    }
}
