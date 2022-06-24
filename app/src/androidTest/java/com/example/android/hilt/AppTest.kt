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

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.hilt.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

/**
 * When a class is annotated with @RunWith or extends a class annotated with @RunWith, JUnit will
 * invoke the class it references to run the tests in that class instead of the runner built into
 * JUnit. [AndroidJUnit4] is a JUnit4 runner for Android tests. This runner offers several features
 * on top of the standard JUnit4 runner:
 *  - Supports running on Robolectric. This implementation will delegate to RobolectricTestRunner if
 *  test is running in Robolectric environment. A custom runner can be provided by specifying the
 *  full class name in a 'android.junit.runner' system property.
 *  - Supports a per-test timeout - specified via a 'timeout_msec' `AndroidJUnitRunner` argument.
 *  - Supports running tests on the application's UI Thread, for tests annotated with UiThreadTest.
 *
 * The `HiltAndroidTest` annotation is used to mark an Android emulator test that requires injection.
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AppTest {

    /**
     * The `@get:Rule` annotation annotates the getter of the kotlin Rule property and is used to
     * annotate the [hiltRule] field to indicate that it references rules or methods that return a
     * rule. The Statement passed to the TestRule will run any Before methods, then the Test method,
     * and finally any After methods, throwing an exception if any of these fail. [HiltAndroidRule]
     * is a [TestRule] for Hilt that can be used with JVM or Instrumentation tests. This rule is
     * required. The Dagger component will not be created without this test rule. A [TestRule] is an
     * alteration in how a test method, or set of test methods, is run and reported. A [TestRule]
     * may add additional checks that cause a test that would otherwise fail to pass, or it may
     * perform necessary setup or cleanup for tests, or it may observe test execution to report it
     * elsewhere. TestRules can do everything that could be done previously with methods annotated
     * with Before, After, BeforeClass, or AfterClass, but they are more powerful, and more easily
     * shared between projects and classes.
     */
    @get:Rule
    var hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    /**
     * The Test annotation tells JUnit that the public void method to which it is attached can be
     * run as a test case. First we call the [ActivityScenario.launch] method with a Java [Class]
     * instance that corresponds to the [MainActivity] KClass and it launches  the [MainActivity]
     * activity and constructs a [ActivityScenario] with the activity, then waits for the lifecycle
     * state transitions to be complete (an [ActivityScenario] provides APIs to start and drive an
     * Activity's lifecycle state for testing). We then perform four UI interactions and check that
     * they cause the expected results:
     *  1. We use the espresso method [withId] to create a matcher that matches the `View` with ID
     *  [R.id.textView] and use that matcher in a call to the [onView] method of espresso to create
     *  a [ViewInteraction] for that view, which we use to call its [ViewInteraction.check] method
     *  to check for the [ViewAssertion] that the view matches the assertion [isDisplayed] (i.e.
     *  the `View` is currently displayed on the screen to the user).
     *  2. We use the espresso method [withId] to create a matcher that matches the `View` with ID
     *  [R.id.button1] and use that matcher in a call to the [onView] method of espresso to create
     *  a [ViewInteraction] for that view, which we use to call its [ViewInteraction.perform] method
     *  to perform the action [click] to click that button.
     *  3. We use the espresso method [withId] to create a matcher that matches the `View` with ID
     *  [R.id.all_logs] and use that matcher in a call to the [onView] method of espresso to create
     *  a [ViewInteraction] for that view, which we use to call its [ViewInteraction.perform] method
     *  to perform the action [click] to click that button.
     *  4. We use the espresso method [withText] to create a matcher that matches the `View` which
     *  contains text containing the substring "Interaction with 'Button 1'" and use that matcher in
     *  a call to the [onView] method of espresso to create a [ViewInteraction] for that view, which
     *  we use to call its [ViewInteraction.check] method to check for the [ViewAssertion] that the
     *  view matches the assertion [isDisplayed] (i.e. the `View` is currently displayed on the
     *  screen to the user).
     */
    @Test
    fun happyPath() {
        // Launches the `MainActivity` activity and constructs ActivityScenario with the activity.
        // Waits for the lifecycle state transitions to be complete. Typically the initial state of
        // the activity is State.RESUMED but can be in another state. For instance, if your activity
        // calls Activity.finish from your Activity.onCreate, the state is State.DESTROYED when this
        // method returns. If you need to supply parameters to the start activity intent, use
        // launch(Intent). This method cannot be called from the main thread except in
        // Robolectric tests.
        ActivityScenario.launch(MainActivity::class.java)

        // Check Buttons fragment screen is displayed
        onView(withId(R.id.textView)).check(matches(isDisplayed()))

        // Tap on Button 1
        onView(withId(R.id.button1)).perform(click())

        // Navigate to Logs screen
        onView(withId(R.id.all_logs)).perform(click())

        // Check Logs fragment screen is displayed
        onView(withText(containsString("Interaction with 'Button 1'")))
            .check(matches(isDisplayed()))
    }
}
