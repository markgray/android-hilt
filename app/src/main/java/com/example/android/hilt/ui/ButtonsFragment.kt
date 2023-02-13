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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.android.hilt.LogApplication
import com.example.android.hilt.R
import com.example.android.hilt.data.Log
import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.data.LoggerLocalDataSource
import com.example.android.hilt.data.LoggerInMemoryDataSource
import com.example.android.hilt.di.InMemoryLogger
import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment that displays buttons whose interactions are recorded. The AndroidEntryPoint annotation
 * Marks an Android component class to be setup for injection with the standard Hilt Dagger Android
 * components. This will generate a base class that the annotated class should extend, either directly
 * or via the Hilt Gradle Plugin (as we do). This base class will take care of injecting members into
 * the Android class as well as handling instantiating the proper Hilt components at the right point
 * in the lifecycle. The name of the base class will be "Hilt_ButtonsFragment.java".
 */
@AndroidEntryPoint
class ButtonsFragment : Fragment() {

    /**
     * The `InMemoryLogger` is an annotation we define in the di/LoggingModule.kt file which is used
     * to `Binds` the construction of a [LoggerDataSource] to an injected [LoggerInMemoryDataSource].
     * in the method `bindInMemoryLogger`, and the `Inject` annotation causes Hilt to populate this
     * field for us automagically in the onAttach() lifecycle method with instances built in the
     * dependencies container that Hilt automatically generated for [ButtonsFragment].
     */
    @InMemoryLogger
    @Inject lateinit var logger: LoggerDataSource

    /**
     * The `Inject` annotation causes Hilt to populate this field for us automagically in the
     * onAttach() lifecycle method with instances built in the dependencies container that Hilt
     * automatically generated for [ButtonsFragment]
     */
    @Inject lateinit var navigator: AppNavigator

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [onCreate] and [onViewCreated]. It is recommended to only inflate the layout in this method
     * and move logic that operates on the returned [View] to [onViewCreated].
     *
     * We return the [View] that our [LayoutInflater] parameter [inflater] inflates from the layout
     * file with ID [R.layout.fragment_buttons] when it uses our [ViewGroup] parameter [container]
     * for the `LayoutParams` without attaching to it.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment,
     * @param container If non-`null`, this is the parent [ViewGroup] that the fragment's
     * UI will be attached to. The fragment should not add the view itself,
     * but this can be used to generate the `LayoutParams` of the view.
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
        return inflater.inflate(R.layout.fragment_buttons, container, false)
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. We locate the [Button] with ID [R.id.button1] in our [View] parameter
     * [view] and set its [View.OnClickListener] to a lambda which calls the [LoggerDataSource.addLog]
     * method of our [logger] field to add a [Log] constructed to contain the message "Interaction
     * with 'Button 1'", locate the [Button] with ID [R.id.button2] in our [View] parameter [view]
     * and set its [View.OnClickListener] to a lambda which calls the [LoggerDataSource.addLog]
     * method of our [logger] field to add a [Log] constructed to contain the message "Interaction
     * with 'Button 2'", and locate the [Button] with ID [R.id.button3] in our [View] parameter
     * [view] and set its [View.OnClickListener] to a lambda which calls the [LoggerDataSource.addLog]
     * method of our [logger] field to add a [Log] constructed to contain the message "Interaction
     * with 'Button 3'". Next we locate the [Button] with ID [R.id.all_logs] in our [View] parameter
     * [view] and set its [View.OnClickListener] to a lambda which calls the [AppNavigator.navigateTo]
     * method of our [navigator] field with the enum [Screens.LOGS] to navigate to the [LogsFragment]
     * and finally locate the [Button] with ID [R.id.delete_logs] in our [View] parameter [view] and
     * set its [View.OnClickListener] to a lambda which calls the [LoggerDataSource.removeLogs]
     * method of our [logger] field to have it remove all of [Log] instances stored in [logger].
     *
     * @param view The [View] returned by [onCreateView].
     * @param savedInstanceState If non-`null`, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.button1).setOnClickListener {
            logger.addLog("Interaction with 'Button 1'")
        }

        view.findViewById<Button>(R.id.button2).setOnClickListener {
            logger.addLog("Interaction with 'Button 2'")
        }

        view.findViewById<Button>(R.id.button3).setOnClickListener {
            logger.addLog("Interaction with 'Button 3'")
        }

        view.findViewById<Button>(R.id.all_logs).setOnClickListener {
            navigator.navigateTo(Screens.LOGS)
        }

        view.findViewById<Button>(R.id.delete_logs).setOnClickListener {
            logger.removeLogs()
        }
    }
}
