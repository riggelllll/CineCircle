package com.koniukhov.cinecirclex.home.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecirclex.core.design.R
import com.koniukhov.cinecirclex.feature.home.R as homeR
import com.koniukhov.cinecirclex.core.common.R as commonR
import com.koniukhov.cinecirclex.util.HiltTestActivity
import com.koniukhov.cinecirclex.util.launchFragmentInHiltContainer
import com.koniukhov.cinecirclex.feature.home.ui.HomeFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
        scenario =
            launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        Espresso.onView(ViewMatchers.withId(homeR.id.tab_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun homeFragment_shouldDisplayViewPager() {
        scenario =
            launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        Espresso.onView(ViewMatchers.withId(homeR.id.view_pager))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun homeFragment_shouldHaveTwoTabs() {
        scenario =
            launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        Espresso.onView(ViewMatchers.withText(commonR.string.movies_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText(commonR.string.tv_series_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun homeFragment_shouldSwitchToTvSeriesTab() {
        scenario =
            launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        Espresso.onView(ViewMatchers.withText(commonR.string.tv_series_title))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(commonR.string.tv_series_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun homeFragment_shouldSwitchBackToMoviesTab() {
        scenario =
            launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_CineCircle)

        Espresso.onView(ViewMatchers.withText(commonR.string.tv_series_title))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(commonR.string.movies_title))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(commonR.string.movies_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}