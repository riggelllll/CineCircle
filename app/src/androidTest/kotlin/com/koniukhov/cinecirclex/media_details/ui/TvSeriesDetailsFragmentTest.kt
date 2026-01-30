package com.koniukhov.cinecirclex.media_details.ui

import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecirclex.feature.media.details.ui.TvSeriesDetailsFragment
import com.koniukhov.cinecirclex.feature.movie_details.R
import com.koniukhov.cinecirclex.util.HiltTestActivity
import com.koniukhov.cinecirclex.util.launchFragmentInHiltContainer
import com.koniukhov.cinecirclex.core.design.R as styleR
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TvSeriesDetailsFragmentTest {

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
    fun tvSeriesDetailsFragment_shouldDisplayBackdropImage() {
        val args = Bundle().apply {
            putLong("tvSeriesId", 1L)
        }

        scenario = launchFragmentInHiltContainer<TvSeriesDetailsFragment>(
            fragmentArgs = args,
            themeResId = styleR.style.Theme_CineCircle
        )

        onView(withId(R.id.img_backdrop))
            .check(matches(isDisplayed()))
    }

    @Test
    fun tvSeriesDetailsFragment_shouldDisplayTitle() {
        val args = Bundle().apply {
            putLong("tvSeriesId", 1L)
        }

        scenario = launchFragmentInHiltContainer<TvSeriesDetailsFragment>(
            fragmentArgs = args,
            themeResId = styleR.style.Theme_CineCircle
        )

        onView(withId(R.id.title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun tvSeriesDetailsFragment_shouldDisplayRating() {
        val args = Bundle().apply {
            putLong("tvSeriesId", 1L)
        }

        scenario = launchFragmentInHiltContainer<TvSeriesDetailsFragment>(
            fragmentArgs = args,
            themeResId = styleR.style.Theme_CineCircle
        )

        onView(withId(R.id.rating))
            .check(matches(isDisplayed()))
    }
}