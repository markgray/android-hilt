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
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import com.example.android.hilt.LogApplication
import com.example.android.hilt.R
import com.example.android.hilt.ServiceLocator
import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.Screens

/**
 * Main activity of the application.
 *
 * Container for the Buttons & Logs fragments. This activity simply tracks clicks on buttons.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var navigator: AppNavigator

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
     * We initialize our [AppNavigator] field [navigator] using the [ServiceLocator.provideNavigator]
     * method of the [LogApplication.serviceLocator] of our app.
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView = findViewById<FrameLayout>(R.id.main_container)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                rightMargin = insets.right
                topMargin = insets.top
                bottomMargin = insets.bottom
            }
            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                supportFragmentManager.popBackStackImmediate()
                if (supportFragmentManager.backStackEntryCount == 0) {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        navigator = (applicationContext as LogApplication).serviceLocator.provideNavigator(this)

        if (savedInstanceState == null) {
            navigator.navigateTo(Screens.BUTTONS)
        }
    }
}
