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

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represents the table in the database. The `Entity` annotation marks this class as
 * an entity. This class will have a mapping SQLite table in the database. Each entity must have at
 * least 1 field annotated with `PrimaryKey` (we use our [id] field for this). Each entity must
 * either have a no-arg constructor or a constructor whose parameters match fields (based on type
 * and name). Constructor does not have to receive all fields as parameters but if a field is not
 * passed into the constructor, it should either be public or have a public setter. If a matching
 * constructor is available, Room will always use it.
 *
 * @param msg the message to be saved in our log table.
 * @param timestamp the current time in milliseconds when the [Log] instance was constructed and
 * added to our database.
 */
@Entity(tableName = "logs")
data class Log(val msg: String, val timestamp: Long) {

    /**
     * The unique primary key for this [Log] `Entity`. The `PrimaryKey` annotation marks a field in
     * an `Entity` as the primary key, and the `autoGenerate` parameter is set to `true` to let
     * SQLite generate the unique id.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
