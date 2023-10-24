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

import android.content.ContentProvider
import android.database.Cursor
import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.hilt.contentprovider.LogsContentProvider
import com.example.android.hilt.ui.ButtonsFragment
import com.example.android.hilt.ui.LogsFragment

/**
 * Data access object to query the database. The `Dao` annotation marks this class as a Data Access
 * Object. Data Access Objects are the main classes where you define your database interactions.
 * They can include a variety of query methods. The class marked with `@Dao` should either be an
 * interface or an abstract class. At compile time, Room will generate an implementation of this
 * class when it is referenced by a Database (in our case this is [LogDao_Impl]).
 */
@Dao
interface LogDao {

    /**
     * The `Query` annotation marks a method in a Dao annotated class as a query method. The value
     * of the annotation includes the query that will be run when this method is called. This query
     * is verified at compile time by Room to ensure that it compiles fine against the database.
     * The arguments of the method will be bound to the bind arguments in the SQL statement.
     *
     * This method returns all of the entries in the `logs` table of our database as a [List] of
     * [Log] objects sorted in descending order by the [Log.id] field. It is called by the
     * [LoggerLocalDataSource.getAllLogs] method, which is called from the `onResume` override of
     * [LogsFragment].
     *
     * @return a [List] of all of the [Log] objects in our database sorted in descending order by
     * the [Log.id] field.
     */
    @Query("SELECT * FROM logs ORDER BY id DESC")
    fun getAll(): List<Log>

    /**
     * The `Insert` annotation marks a method in a Dao annotated class as an insert method.
     * The implementation of the method will insert its parameters into the database.
     * All of the parameters of the Insert method must either be classes annotated with `Entity`
     * or collections/array of it. It is called by the [LoggerLocalDataSource.addLog] method,
     * which is called from the `OnClickListener` of the three buttons ("Button 1", "Button 2",
     * and "Button 3" in the [ButtonsFragment] UI.
     *
     * @param logs the `vararg` or array of [Log] objects to insert in our database.
     */
    @Insert
    fun insertAll(vararg logs: Log)

    /**
     * Deletes all of the rows in the `logs` table from the database. It is called from the
     * [LoggerLocalDataSource.removeLogs] method which is called from the `OnClickListener`
     * of the "Delete Logs" button in the [ButtonsFragment] UI.
     */
    @Query("DELETE FROM logs")
    fun nukeTable()

    /**
     * This `Query` returns a [Cursor] pointing to all of the entries in the `logs` table of our
     * database instead of a list of [Log] like our [getAll] method. It is called by the
     * [ContentProvider.query] method of our [LogsContentProvider] when it receives a query
     * for the [Uri] "content://com.example.android.hilt.provider/logs" from a client.
     *
     * @return a [Cursor] object which allows access to all of the [Log] objects in our database
     * sorted in descending order by the [Log.id] field.
     */
    @Query("SELECT * FROM logs ORDER BY id DESC")
    fun selectAllLogsCursor(): Cursor

    /**
     * This `Query` returns a [Cursor] pointing to the entry in the `logs` table of our
     * database whose [Log.id] field matches our [id] parameter. It is called by the
     * [ContentProvider.query] method of our [LogsContentProvider] when it receives a query
     * for the [Uri] "content://com.example.android.hilt.provider/logs/&#42;" from a client,
     * where the [id] is parsed from the "&#42;" substring of the [Uri].
     *
     * @return a [Cursor] object which allows access to the [Log] object in our database whose
     * [Log.id] field matches our [id] parameter.
     */
    @Query("SELECT * FROM logs WHERE id = :id")
    fun selectLogById(id: Long): Cursor
}
