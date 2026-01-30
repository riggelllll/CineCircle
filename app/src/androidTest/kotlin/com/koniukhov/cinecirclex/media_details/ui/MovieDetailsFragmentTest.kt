package com.koniukhov.cinecirclex.media_details.ui

import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecirclex.core.design.R
import com.koniukhov.cinecirclex.feature.movie_details.R as movieDetailsR
import com.koniukhov.cinecirclex.feature.media.details.ui.MovieDetailsFragment
import com.koniukhov.cinecirclex.util.HiltTestActivity
import com.koniukhov.cinecirclex.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MovieDetailsFragmentTest {

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
    fun movieDetailsFragment_shouldDisplayBackdropImage() {
        scenario = launchFragmentInHiltContainer<MovieDetailsFragment>(
            fragmentArgs = Bundle().apply {
                putInt("movieId", 1)
            },
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(movieDetailsR.id.img_backdrop))
            .check(matches(isDisplayed()))
    }

    @Test
    fun movieDetailsFragment_shouldDisplayMoviePoster() {
        scenario = launchFragmentInHiltContainer<MovieDetailsFragment>(
            fragmentArgs = Bundle().apply {
                putInt("movieId", 1)
            },
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(movieDetailsR.id.img_backdrop))
            .check(matches(isDisplayed()))
    }

    @Test
    fun movieDetailsFragment_shouldDisplayMovieTitle() {
        scenario = launchFragmentInHiltContainer<MovieDetailsFragment>(
            fragmentArgs = Bundle().apply {
                putInt("movieId", 1)
            },
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(movieDetailsR.id.movie_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun movieDetailsFragment_shouldDisplayMovieOverview() {
        scenario = launchFragmentInHiltContainer<MovieDetailsFragment>(
            fragmentArgs = Bundle().apply {
                putInt("movieId", 1)
            },
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(movieDetailsR.id.plot_description))
            .check(matches(isDisplayed()))
    }

    @Test
    fun movieDetailsFragment_shouldDisplayMovieRating() {
        scenario = launchFragmentInHiltContainer<MovieDetailsFragment>(
            fragmentArgs = Bundle().apply {
                putInt("movieId", 1)
            },
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(R.id.rating))
            .check(matches(isDisplayed()))
    }
}