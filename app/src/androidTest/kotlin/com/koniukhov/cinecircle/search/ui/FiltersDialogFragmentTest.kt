package com.koniukhov.cinecircle.search.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecircle.core.design.R as DesignR
import com.koniukhov.cinecircle.feature.search.ui.FiltersDialogFragment
import com.koniukhov.cinecircle.util.HiltTestActivity
import com.koniukhov.cinecircle.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.koniukhov.cinecircle.core.common.R as commonR
import com.koniukhov.cinecircle.feature.search.R

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FiltersDialogFragmentTest {

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
    fun filtersDialog_shouldDisplayToolbar() {
        scenario = launchFragmentInHiltContainer<FiltersDialogFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun filtersDialog_shouldDisplayTabLayout() {
        scenario = launchFragmentInHiltContainer<FiltersDialogFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.tab_layout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun filtersDialog_shouldDisplayViewPager() {
        scenario = launchFragmentInHiltContainer<FiltersDialogFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.view_pager))
            .check(matches(isDisplayed()))
    }

    @Test
    fun filtersDialog_shouldHaveMoviesTab() {
        scenario = launchFragmentInHiltContainer<FiltersDialogFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withText(commonR.string.movies_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun filtersDialog_shouldHaveTvSeriesTab() {
        scenario = launchFragmentInHiltContainer<FiltersDialogFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withText(commonR.string.tv_series_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun filtersDialog_shouldSwitchToTvSeriesTab() {
        scenario = launchFragmentInHiltContainer<FiltersDialogFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withText(commonR.string.tv_series_title))
            .perform(click())

        onView(withText(commonR.string.tv_series_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun filtersDialog_shouldSwitchBackToMoviesTab() {
        scenario = launchFragmentInHiltContainer<FiltersDialogFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withText(commonR.string.tv_series_title))
            .perform(click())

        onView(withText(commonR.string.movies_title))
            .perform(click())

        onView(withText(commonR.string.movies_title))
            .check(matches(isDisplayed()))
    }
}