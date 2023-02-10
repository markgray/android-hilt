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
     * The function parameters are the dependencies of that type. This code tells Hilt that
     * database.logDao() needs to be executed when providing an instance of LogDao. Since we have
     * AppDatabase as a transitive dependency, we also need to tell Hilt how to provide instances
     * of that type as we do below.
     *
     * @param database our singleon [AppDatabase]
     * @return the [LogDao] for the "logs" table of our database
     */
    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }

    /**
     * The `Provides` annotation tells Hilt how to provide types that cannot be constructor injected.
     * The function body of a function that is annotated with @Provides will be executed every time
     * Hilt needs to provide an instance of that type. The return type of the `Provides`-annotated
     * function tells Hilt the binding type, the type that the function provides instances of.
     * The function parameters are the dependencies of that type. The `Singleton` annotation
     * identifies this as a type that the injector only instantiates once. The `ApplicationContext`
     * annotation tells Hilt to use the default application context binding to provide the [Context]
     * dependency [appContext].
     *
     * @param appContext the application [Context] supplied by Hilt
     * @return our singleton [AppDatabase] instance.
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
}


