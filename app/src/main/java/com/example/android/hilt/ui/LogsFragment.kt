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

package com.example.android.hilt.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.android.hilt.R
import com.example.android.hilt.data.Log
import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.data.LoggerInMemoryDataSource
import com.example.android.hilt.di.InMemoryLogger
import com.example.android.hilt.util.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.internal.GeneratedComponentManagerHolder
import javax.inject.Inject

/**
 * Fragment that displays the database logs. The `AndroidEntryPoint` annotation marks this class to
 * be setup for injection with the standard Hilt Dagger Android components. This annotation will
 * generate a base class that the annotated class should extend, either directly or via the Hilt
 * Gradle Plugin (as we do). This base class will take care of injecting members into the Android
 * class as well as handling instantiating the proper Hilt components at the right point in the
 * lifecycle. The name of the base class will be "Hilt_LogsFragment.java" which extends
 * [Fragment] and implements [GeneratedComponentManagerHolder]. When using the Gradle plugin, this
 * is swapped as the base class via bytecode transformation.
 */
@AndroidEntryPoint
class LogsFragment : Fragment() {

    /**
     * The `InMemoryLogger` is an annotation we define in the di/LoggingModule.kt file which is used
     * to `Binds` the construction of a [LoggerDataSource] to an injected [LoggerInMemoryDataSource].
     * in the method `bindInMemoryLogger`, and the `Inject` annotation causes Hilt to populate this
     * field for us automagically in the onAttach() lifecycle method with instances built in the
     * dependencies container that Hilt automatically generated for LogsFragment.
     */
    @InMemoryLogger
    @Inject
    lateinit var logger: LoggerDataSource

    /**
     * The `Inject` annotation causes Hilt to populate this field for us automagically in the
     * onAttach() lifecycle method with instances built in the dependencies container that Hilt
     * automatically generated for LogsFragment. The [DateFormatter] zero argument constructor is
     * annotated with `Inject` so Hilt will use it to populate this field for us automagically in
     * the onAttach() lifecycle method with instances built in the dependencies container that Hilt
     * automatically generated for LogsFragment.
     */
    @Inject
    lateinit var dateFormatter: DateFormatter

    /**
     * This is the [RecyclerView] in our layout file with the ID [R.id.recycler_view] that we use to
     * display the [Log] entries in our database.
     */
    private lateinit var recyclerView: RecyclerView

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onViewCreated]. It is recommended to only inflate the layout in this method
     * and move logic that operates on the returned [View] to [onViewCreated].
     *
     * We return the [View] that our [LayoutInflater] parameter [inflater] inflates from the layout
     * file with ID [R.layout.fragment_logs] when it uses our [ViewGroup] parameter [container]
     * for the `LayoutParams` without attaching to it.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment,
     * @param container If non-`null`, this is the parent [ViewGroup] that the fragment's
     * UI will be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-`null`, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the [View] for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logs, container, false)
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. We initialize our [RecyclerView] field [recyclerView] by finding
     * the [View] with ID [R.id.recycler_view] in our [View] parameter [view] and use the [apply]
     * extension function to call its [RecyclerView.setHasFixedSize] method with the value `true`
     * to inform it that adapter changes cannot affect the size of the [RecyclerView] (which allows
     * it to perform several optimizations).
     *
     * @param view The [View] returned by [onCreateView].
     * @param savedInstanceState If non-`null`, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
        }
    }

    /**
     * Called when the fragment is visible to the user and actively running. First we call our
     * super's implementation of `onResume`, then we call the [LoggerDataSource.getAllLogs] method
     * of our field [logger] with a lambda that sets the adapter of our [RecyclerView] field
     * [recyclerView] to an instance of [LogsViewAdapter] constructed to use the [List] of [Log]
     * objects retrieved from [logger] and to format the [Log.timestamp] of each [Log] using our
     * [DateFormatter] field [dateFormatter] when it includes the timestamp in the text that is
     * displayed in the item view used to display the [Log].
     */
    override fun onResume() {
        super.onResume()

        logger.getAllLogs { logs: List<Log> ->
            recyclerView.adapter =
                LogsViewAdapter(
                    logs,
                    dateFormatter
                )
        }
    }
}

/**
 * RecyclerView adapter for the logs list.
 *
 * @param logsDataSet the [List] of [Log] objects that will serve as our dataset.
 * @param daterFormatter the [DateFormatter] to use to format the [Log.timestamp] field when
 * we display each [Log] object in a [TextView].
 */
private class LogsViewAdapter(
    private val logsDataSet: List<Log>,
    private val daterFormatter: DateFormatter
) : RecyclerView.Adapter<LogsViewAdapter.LogsViewHolder>() {

    /**
     * This is the custom [RecyclerView.ViewHolder] we use to hold each [TextView] that we use as
     * the [itemView] of the [Log] objects in our dataset.
     *
     * @param textView the [TextView] that displays the info contained in our [Log] entry.
     */
    class LogsViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    /**
     * Called when [RecyclerView] needs a new [RecyclerView.ViewHolder] of the given type to represent
     * an item. We return the [View] that the [LayoutInflater] from the context of our [ViewGroup]
     * parameter [parent] inflates from the layout file with ID [R.layout.text_row_item] when it uses
     * our [ViewGroup] parameter [parent] for the `LayoutParams` without attaching to it.
     *
     * @param parent The [ViewGroup] into which the new [View] will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new [View].
     * @return A new [LogsViewHolder] that holds a [View] of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        return LogsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.text_row_item, parent, false) as TextView
        )
    }

    /**
     * Returns the total number of items in the data set held by the adapter. We just return the
     * size of our [List] of [Log] field [logsDataSet].
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return logsDataSet.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [LogsViewHolder.itemView] to reflect the item at the given
     * position. We initialize our [Log] variable `val log` to the entry at position [position]
     * in our [List] of [Log] field [logsDataSet], then set the text of the [TextView] in our
     * [LogsViewHolder] parameter [holder] to a string formed from the information contained in the
     * fields of `log`.
     *
     * @param holder The [LogsViewHolder] which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n") // TODO: This is a Locale.US only app?
    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val log: Log = logsDataSet[position]
        holder.textView.text = "${log.msg}\n\t${daterFormatter.formatDate(log.timestamp)}"
    }
}
