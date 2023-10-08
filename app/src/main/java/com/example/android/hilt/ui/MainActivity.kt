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

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.android.hilt.R
import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import com.example.android.hilt.navigator.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main activity of the application. Container for the Buttons & Logs fragments. This activity
 * simply tracks clicks on buttons. The AndroidEntryPoint annotation Marks an Android component
 * class to be setup for injection with the standard Hilt Dagger Android components. This will
 * generate a base class that the annotated class should extend, either directly or via the Hilt
 * Gradle Plugin (as we do). This base class will take care of injecting members into the Android
 * class as well as handling instantiating the proper Hilt components at the right point in the
 * lifecycle. The name of the base class will be "Hilt_MainActivity.java".
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /**
     * This is annotated with the `Inject` so that Hilt will provide an instance of it using the
     * `bindNavigator` method in the file di/NavigationModule.kt and the [AppNavigatorImpl] instance
     * it provides implements the [AppNavigator.navigateTo] method which we use to navigate to the
     * fragment we want to be showing.
     */
    @Inject
    lateinit var navigator: AppNavigator

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`,
     * then we set our content view to our layout file [R.layout.activity_main] which consists of
     * just a [FrameLayout] whose ID is [R.id.main_container]. Next we initialize our
     * [OnBackPressedCallback] variable `val callback` to an instance which overrides the
     * [OnBackPressedCallback.handleOnBackPressed] method to first call the
     * [FragmentManager.popBackStackImmediate] method of the `supportFragmentManager`, then if its
     * [FragmentManager.getBackStackEntryCount] returns 0 its calls the [finish] method to close the
     * Activity.
     *
     * Then if our [Bundle] parameter [savedInstanceState] is `null` this is the first time we are
     * being called so we call the [AppNavigator.navigateTo] method of our field [navigator] to have
     * it navigate to the [Screens.BUTTONS] fragment (aka [ButtonsFragment]). If [savedInstanceState]
     * is not `null` we are being restarted after a configuration change and the system will see that
     * the previous fragment is restored.
     *
     * @param savedInstanceState If non-`null` this activity is being re-constructed from a
     * previous saved state as given here, and we use this fact to skip navigating to one of
     * our fragments if we are being re-constructed since the system will see to it that the
     * previous fragment is restored for us.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                supportFragmentManager.popBackStackImmediate()
                if (supportFragmentManager.backStackEntryCount == 0) {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        if (savedInstanceState == null) {
            navigator.navigateTo(Screens.BUTTONS)
        }
    }
}
