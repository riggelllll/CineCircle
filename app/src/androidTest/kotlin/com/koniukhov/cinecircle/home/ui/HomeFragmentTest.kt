package com.koniukhov.cinecircle.home.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.feature.home.ui.HomeFragment
import com.koniukhov.cinecircle.util.HiltTestActivity
import com.koniukhov.cinecircle.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.koniukhov.cinecircle.feature.home.R as homeR

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var scenario: ActivityScenario<HiltTestActivity>

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun teardown() {
        if (::scenario.isInitialized) {
            scenario.close()
        }
    }

    @Test
    fun homeFragment_shouldDisplayTabLayout() {
        scenario = launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        onView(withId(homeR.id.tab_layout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun homeFragment_shouldDisplayViewPager() {
        scenario = launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        onView(withId(homeR.id.view_pager))
            .check(matches(isDisplayed()))
    }

    @Test
    fun homeFragment_shouldHaveTwoTabs() {
        scenario = launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        onView(withText(com.koniukhov.cinecircle.core.common.R.string.movies_title))
            .check(matches(isDisplayed()))

        onView(withText(com.koniukhov.cinecircle.core.common.R.string.tv_series_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun homeFragment_shouldSwitchToTvSeriesTab() {
        scenario = launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        onView(withText(com.koniukhov.cinecircle.core.common.R.string.tv_series_title))
            .perform(click())

        onView(withText(com.koniukhov.cinecircle.core.common.R.string.tv_series_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun homeFragment_shouldSwitchBackToMoviesTab() {
        scenario = launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        onView(withText(com.koniukhov.cinecircle.core.common.R.string.tv_series_title))
            .perform(click())

        onView(withText(com.koniukhov.cinecircle.core.common.R.string.movies_title))
            .perform(click())

        onView(withText(com.koniukhov.cinecircle.core.common.R.string.movies_title))
            .check(matches(isDisplayed()))
    }
}