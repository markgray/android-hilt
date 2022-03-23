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

@file:Suppress("unused")

package com.example.android.hilt.di

import android.app.Activity
import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * The `InstallIn` annotation declares which component(s) the annotated class should be included in
 * when Hilt generates the components. Installing a module into a component allows that binding to
 * be accessed as a dependency of other bindings in that component or any child component(s) below
 * it in the component hierarchy. They can also be accessed from the corresponding `AndroidEntryPoint`
 * classes. Being installed in a component also allows that binding to be scoped to that component.
 * The [ActivityComponent] is used because [AppNavigator] needs information specific to the activity
 * because [AppNavigatorImpl] has an [Activity] as a dependency. The `Module` annotation Annotates
 * this as a class that contributes to the object graph and informs Hilt how to provide instances of
 * types which cannot be constructor injected.
 */
@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {

    /**
     * The `Binds` annotation tells Hilt which implementation to use when it needs to provide an
     * instance of an interface. The function return type tells Hilt what interface the function
     * provides instances of. The function parameter tells Hilt which implementation to provide.
     * [AppNavigatorImpl] implements [AppNavigator] and is constructor injected.
     */
    @Binds
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}
