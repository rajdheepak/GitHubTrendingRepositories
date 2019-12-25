package com.zestworks.githubtrendingrepositories

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GithubUiAutomationTests {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun assertUiElementsVisibility() {
        onView(withId(R.id.github_list_view)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.error_page_layout)).check(matches(Matchers.not(ViewMatchers.isDisplayed())))
        rotateScreen(activityRule.activity)
        onView(withId(R.id.github_list_view)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.error_page_layout)).check(matches(Matchers.not(ViewMatchers.isDisplayed())))
    }

    private fun rotateScreen(activity: Activity) {
        val orientation = activity.applicationContext.resources.configuration.orientation
        activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

}