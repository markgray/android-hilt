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
 * instances of [LoggerDataSource], when that dependency is annotated with `InMemoryLogger`
 */
@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    /**
     * The `Binds` annotation tells Hilt to provide an instance of [LoggerInMemoryDataSource] when
     * a [LoggerDataSource] dependency labeled with the `InMemoryLogger` annotation is encountered,
     * and the `ActivityScoped` annotation indicates to Hilt the binding should exist for the life
     * of an activity.
     */
    @InMemoryLogger
    @ActivityScoped
    @Binds
    abstract fun bindInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}
