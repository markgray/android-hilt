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

import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.data.LoggerInMemoryDataSource
import com.example.android.hilt.data.LoggerLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * The `Qualifier` annotation identifies qualifier annotations. A qualifier is an annotation used to
 * identify a binding. When an `Inject` annotation for a [LoggerDataSource] dependency is annotated
 * with [InMemoryLogger] the [LoggingInMemoryModule.bindInMemoryLogger] method is used by Hilt to
 * construct the [LoggerDataSource] instance required.
 */
@Qualifier
annotation class InMemoryLogger

/**
 * The `Qualifier` annotation identifies qualifier annotations. A qualifier is an annotation used to
 * identify a binding. When an `Inject` annotation for a [LoggerDataSource] dependency is annotated
 * with [DatabaseLogger] the [LoggingDatabaseModule.bindDatabaseLogger] method is used by Hilt to
 * construct the [LoggerDataSource] instance required.
 */
@Qualifier
annotation class DatabaseLogger

/**
 * The `InstallIn` annotation declared that this object should be included in the Application class,
 * and the `Module` annotation is used to signal to Hilt that this class tells Hilt how to provide
 * instances of [LoggerDataSource], when that dependency is annotated with `DatabaseLogger`.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class LoggingDatabaseModule {

    /**
     * The `Binds` annotation tells Hilt to provide an instance of [LoggerLocalDataSource] when
     * a [LoggerDataSource] dependency labeled with the `DatabaseLogger` annotation is encountered,
     * and the `Singleton` annotation indicates to Hilt that the injector only instantiates once.
     */
    @DatabaseLogger
    @Singleton
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource
}

/**
 * The `InstallIn` annotation declared that this object should be included in the Activity class,
 * and the `Module` annotation is used to signal to Hilt that this class tells Hilt how to provide
 * instances of [LoggerDataSource], when that dependency is annotated with `InMemoryLogger`.
 */
@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    /**
     * The `Binds` annotation tells Hilt to provide an instance of [LoggerInMemoryDataSource] when
     * a [LoggerDataSource] dependency labeled with the `DatabaseLogger` annotation is encountered,
     * and the `ActivityScoped` annotation indicates to Hilt the binding should exist for the life
     * of an activity.
     */
    @InMemoryLogger
    @ActivityScoped
    @Binds
    abstract fun bindInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}
