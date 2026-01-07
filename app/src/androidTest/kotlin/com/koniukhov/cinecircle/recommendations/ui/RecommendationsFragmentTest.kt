package com.koniukhov.cinecircle.recommendations.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.feature.ai_recommendations.R as recommendationsR
import com.koniukhov.cinecircle.feature.ai.recommendations.ui.RecommendationsFragment
import com.koniukhov.cinecircle.util.HiltTestActivity
import com.koniukhov.cinecircle.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RecommendationsFragmentTest {

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
    fun recommendationsFragment_shouldContainRecyclerView() {
        scenario = launchFragmentInHiltContainer<RecommendationsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(recommendationsR.id.recommendations_recycler_view))
            .check(matches(isAssignableFrom(androidx.recyclerview.widget.RecyclerView::class.java)))
    }

    @Test
    fun recommendationsFragment_shouldContainProgressBar() {
        scenario = launchFragmentInHiltContainer<RecommendationsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(recommendationsR.id.progress_bar))
            .check(matches(isAssignableFrom(android.widget.ProgressBar::class.java)))
    }

    @Test
    fun recommendationsFragment_shouldContainEmptyStateLayout() {
        scenario = launchFragmentInHiltContainer<RecommendationsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(recommendationsR.id.empty_state_layout))
            .check(matches(isAssignableFrom(android.widget.LinearLayout::class.java)))
    }

    @Test
    fun recommendationsFragment_shouldContainTitle() {
        scenario = launchFragmentInHiltContainer<RecommendationsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(recommendationsR.id.recommendations_title))
            .check(matches(isAssignableFrom(android.widget.TextView::class.java)))
    }
}