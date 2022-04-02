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

package com.example.android.hilt.di

import android.content.Context
import androidx.room.Room
import com.example.android.hilt.data.AppDatabase
import com.example.android.hilt.data.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The `InstallIn` annotation declared that this object should be included in the Application class,
 * and the `Module` annotation is used to signal to Hilt that this class tells Hilt how to provide
 * instances of [AppDatabase] and [LogDao].
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    /**
     * The `Provides` annotation tells Hilt how to provide types that cannot be constructor injected.
     * The function body of a function that is annotated with @Provides will be executed every time
     * Hilt needs to provide an instance of that type. The return type of the `Provides`-annotated
     * function tells Hilt the binding type, the type that the function provides instances of.
     * The function parameters are the dependencies of that type. The `Singleton` annotation
     * identifies this as a type that the injector only instantiates once. The `ApplicationContext`
     * annotation tells Hilt to use the default application context binding to provide the [Context]
     * dependency [appContext].
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "logging.db"
        ).build()
    }

    /**
     * The `Provides` annotation tells Hilt how to provide types that cannot be constructor injected.
     * The function body of a function that is annotated with @Provides will be executed every time
     * Hilt needs to provide an instance of that type. The return type of the `Provides`-annotated
     * function tells Hilt the binding type, the type that the function provides instances of.
     * The function parameters are the dependencies of that type. This code tells Hilt that
     * database.logDao() needs to be executed when providing an instance of LogDao. Since we have
     * AppDatabase as a transitive dependency, we also need to tell Hilt how to provide instances
     * of that type as we do above.
     */
    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }
}
