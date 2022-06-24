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

package com.example.android.hilt.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.android.hilt.data.LogDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.lang.UnsupportedOperationException

/** The path of this content provider.  */
private const val LOGS_TABLE = "logs"

/** The authority of this content provider.  */
private const val AUTHORITY = "com.example.android.hilt.provider"

/** The match code for some items in the Logs table.  */
private const val CODE_LOGS_DIR = 1

/** The match code for an item in the Logs table.  */
private const val CODE_LOGS_ITEM = 2

/**
 * A ContentProvider that exposes the logs outside the application process.
 */
class LogsContentProvider : ContentProvider() {

    /**
     * The `EntryPoint` annotation marks this interface as an entry point into a generated component.
     * This annotation must be used with `InstallIn` to indicate which component(s) should have this
     * entry point, in our case it is installed in the `SingletonComponent` since we want the
     * dependency from an instance of the Application container. When assembling components, Hilt
     * will make the indicated components extend the interface marked with the `EntryPoint` annotation.
     * Our [getLogDao] method uses the [EntryPointAccessors.fromApplication] to retrieve the
     * entry point interface from our application. Then it retrieves a [LogDao] instance provided by
     * Hilt using the [LogsContentProviderEntryPoint.logDao] method of the
     * [LogsContentProviderEntryPoint] implementation it returns.
     */
    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface LogsContentProviderEntryPoint {
        /**
         *
         */
        fun logDao(): LogDao
    }

    /**
     * [UriMatcher] that we use to aid us in matching URIs received by our [query] method. Its
     * [UriMatcher.match] method will return [CODE_LOGS_DIR] when it matches the URI:
     *
     * "content://com.example.android.hilt.provider/logs"
     *
     * and it will return [CODE_LOGS_ITEM] when it matches the URI:
     *
     * "content://com.example.android.hilt.provider/logs/&#42;"
     */
    private val matcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, LOGS_TABLE, CODE_LOGS_DIR)
        addURI(AUTHORITY, "$LOGS_TABLE/*", CODE_LOGS_ITEM)
    }

    /**
     * We implement this to initialize our content provider on startup. This method is called for
     * all registered content providers on the application main thread at application launch time.
     * It must not perform lengthy operations, or application startup will be delayed.
     *
     * @return `true` if the provider was successfully loaded, `false` otherwise, we always return
     * `true`.
     */
    override fun onCreate(): Boolean {
        return true
    }

    /**
     * Queries all the logs or an individual log from the logs database. We initialize our [Int]
     * variable `val code` to the value that the [UriMatcher.match] method of [matcher] returns when
     * it tries to match against the path in our [Uri] parameter [uri]. If `code` is anything other
     * than [CODE_LOGS_DIR] or [CODE_LOGS_ITEM] we throw [IllegalArgumentException] "Unknown URI:".
     * Otherwise we initialize our [Context] variable `val appContext` to the context of the single,
     * global `Application` object of the current process (throwing [IllegalStateException] if this
     * is `null`). Next we initialize our [LogDao] variable `val logDao` to the instance that is
     * returned by our [getLogDao] method when it is passed `appContext`. We initialize our [Cursor]
     * variable `val cursor` to the [Cursor] returned by the [LogDao.selectAllLogsCursor] method of
     * `logDao` if `code` is equal to [CODE_LOGS_DIR] or to the [Cursor] returned for the [Long] that
     * the [ContentUris.parseId] method parses from the last segment of [uri] when that value is
     * passed to the [LogDao.selectLogById] method of `logDao` otherwise. Finally we call the
     * [Cursor.setNotificationUri] method of `cursor` to register it to watch for changes to `uri`,
     * and return `cursor` to the caller.
     *
     * @param uri The URI to query. This will be the full URI sent by the client; if the client is
     * requesting a specific record, the URI will end in a record number that the implementation
     * should parse and add to a WHERE or HAVING clause, specifying that _id value.
     * @param projection The list of columns to put into the cursor. If `null` all columns are
     * included.
     * @param selection A selection criteria to apply when filtering rows. If `null` then all rows
     * are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     * from [selectionArgs], in order that they appear in the selection. The values will be bound
     * as Strings.
     * @param sortOrder How the rows in the cursor should be sorted. If `null` then the provider is
     * free to define the sort order.
     * @return a [Cursor] or `null`.
     */
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code: Int = matcher.match(uri)
        return if (code == CODE_LOGS_DIR || code == CODE_LOGS_ITEM) {
            val appContext: Context = context?.applicationContext ?: throw IllegalStateException()
            val logDao: LogDao = getLogDao(appContext)

            val cursor: Cursor? = if (code == CODE_LOGS_DIR) {
                logDao.selectAllLogsCursor()
            } else {
                logDao.selectLogById(ContentUris.parseId(uri))
            }
            cursor?.setNotificationUri(appContext.contentResolver, uri)
            cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    /**
     * Gets a LogDao instance provided by Hilt using the @EntryPoint annotated interface. We initialize
     * our [LogsContentProviderEntryPoint] variable `val hiltEntryPoint` using the method
     * [EntryPointAccessors.fromApplication] to retrieve the [LogsContentProviderEntryPoint] entry
     * point interface from our application. We then return the [LogDao] that the `logDao` method
     * of `hiltEntryPoint` returns.
     *
     * @param appContext the context of the single, global `Application` object of the current
     * process.
     * @return the [LogDao] that Hilt provides from the [LogsContentProviderEntryPoint] interface
     * thanks to the @EntryPoint annotation.
     */
    private fun getLogDao(appContext: Context): LogDao {
        val hiltEntryPoint: LogsContentProviderEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            LogsContentProviderEntryPoint::class.java
        )
        return hiltEntryPoint.logDao()
    }

    /**
     * Implement this to handle requests to insert a new row. We throw [UnsupportedOperationException]
     * "Only reading operations are allowed".
     *
     * @param uri The content:// URI of the insertion request.
     * @param values A set of column_name/value pairs to add to the database.
     * @return The URI for the newly inserted item.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }

    /**
     * Implement this to handle requests to update one or more rows. We just throw
     * [UnsupportedOperationException] "Only reading operations are allowed".
     *
     * @param uri The URI to query. This can potentially have a record ID if
     * this is an update request for a specific record.
     * @param values A set of column_name/value pairs to update in the database.
     * @param selection An optional filter to match rows to update.
     * @return the number of rows affected.
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }

    /**
     * Implement this to handle requests to delete one or more rows. We just throw
     * [UnsupportedOperationException] "Only reading operations are allowed".
     *
     * @param uri The full URI to query, including a row ID (if a specific
     * record is requested).
     * @param selection An optional restriction to apply to rows when deleting.
     * @return The number of rows affected.
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the given URI. We throw
     * [UnsupportedOperationException] "Only reading operations are allowed".
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }
}
