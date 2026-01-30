package com.koniukhov.cinecirclex.recommendations.ui

import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecirclex.core.design.R
import com.koniukhov.cinecirclex.feature.ai.recommendations.ui.RecommendationsFragment
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

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.ai_recommendations.R.id.recommendations_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isAssignableFrom(RecyclerView::class.java)))
    }

    @Test
    fun recommendationsFragment_shouldContainProgressBar() {
        scenario = launchFragmentInHiltContainer<RecommendationsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.ai_recommendations.R.id.progress_bar))
            .check(ViewAssertions.matches(ViewMatchers.isAssignableFrom(ProgressBar::class.java)))
    }

    @Test
    fun recommendationsFragment_shouldContainEmptyStateLayout() {
        scenario = launchFragmentInHiltContainer<RecommendationsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.ai_recommendations.R.id.empty_state_layout))
            .check(ViewAssertions.matches(ViewMatchers.isAssignableFrom(LinearLayout::class.java)))
    }

    @Test
    fun recommendationsFragment_shouldContainTitle() {
        scenario = launchFragmentInHiltContainer<RecommendationsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.ai_recommendations.R.id.recommendations_title))
            .check(ViewAssertions.matches(ViewMatchers.isAssignableFrom(TextView::class.java)))
    }
}