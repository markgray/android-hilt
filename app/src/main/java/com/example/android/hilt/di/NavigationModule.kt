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
