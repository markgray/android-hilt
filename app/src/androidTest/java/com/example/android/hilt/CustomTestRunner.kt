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

package com.example.android.hilt

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import kotlin.reflect.KClass

/**
 * This is the custom `testInstrumentationRunner` that needs to be used for running instrumented
 * tests on a Hilt injected application and the app/build.gradle specifies it as such in the
 * `defaultConfig` block of its `android` block.
 */
@Suppress("unused") // Used in app/build.gradle
class CustomTestRunner : AndroidJUnitRunner() {
    /**
     * Perform instantiation of the process's [Application] object. The default implementation
     * provides the normal system behavior, and the only change necessary is passing the name
     * of the java class instance corresponding to the [KClass] instance [HiltTestApplication].
     *
     * @param cl The ClassLoader with which to instantiate the object.
     * @param name The name of the class implementing the Application object.
     * @param context The context to initialize the application with
     *
     * @return The newly instantiated Application object.
     */
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
