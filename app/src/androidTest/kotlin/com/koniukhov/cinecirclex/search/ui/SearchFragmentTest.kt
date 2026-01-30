package com.koniukhov.cinecirclex.search.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecirclex.core.design.R as DesignR
import com.koniukhov.cinecirclex.feature.search.ui.SearchFragment
import com.koniukhov.cinecirclex.util.HiltTestActivity
import com.koniukhov.cinecirclex.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.koniukhov.cinecirclex.feature.search.R

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

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
    fun searchFragment_shouldDisplaySearchBar() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.search_bar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchFragment_shouldDisplaySearchView() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.search_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchFragment_shouldNotDisplayFilterRecyclerView() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.filters_recycler_view))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun searchFragment_shouldDisplayEmptyView() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.empty_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchFragment_shouldDisplayAppBarLayout() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.app_bar_layout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchFragment_filtersRecyclerView_shouldBeInitiallyGone() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.filters_recycler_view))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun searchFragment_filterButton_shouldBeClickable() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.filter_button))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun searchFragment_shouldDisplayFiltersButton() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.filter_button))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchFragment_shouldClickFiltersButton() {
        scenario = launchFragmentInHiltContainer<SearchFragment>(
            themeResId = DesignR.style.Theme_CineCircle
        )

        onView(withId(R.id.filter_button))
            .perform(click())
    }
}