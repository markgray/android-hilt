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

package com.example.android.hilt.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * String formatter for the log dates. The `Inject` annotation identifies our constructor as a
 * injectable constructor to Hilt and Hilt generates a [DateFormatter_Factory] java class from
 * this file which it will use when a [DateFormatter] class injection is requested..
 */
class DateFormatter @Inject constructor() {

    /**
     * This is a [SimpleDateFormat] for the [Locale.US] locale.
     */
    private val formatter = SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.US)

    /**
     * Formats a [Date] constructed from our [timestamp] parameter into a date/time string using the
     * [SimpleDateFormat.format] method of our field [formatter] and returns that [String] to the
     * caller.
     *
     * @param timestamp the number of milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return a human friendly [String] representation of the [Date] that the [timestamp] parameter
     * represents.
     */
    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
