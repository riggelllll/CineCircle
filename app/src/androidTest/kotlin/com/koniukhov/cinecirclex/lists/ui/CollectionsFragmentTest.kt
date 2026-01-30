package com.koniukhov.cinecirclex.lists.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecirclex.core.design.R
import com.koniukhov.cinecirclex.feature.lists.ui.CollectionsFragment
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
class CollectionsFragmentTest {

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
    fun collectionsFragment_shouldDisplayRecyclerView() {
        scenario = launchFragmentInHiltContainer<CollectionsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.collections.R.id.recycler_collections))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun collectionsFragment_shouldDisplayAddCollectionFab() {
        scenario = launchFragmentInHiltContainer<CollectionsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.collections.R.id.fab_add))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun collectionsFragment_shouldClickAddCollectionFab() {
        scenario = launchFragmentInHiltContainer<CollectionsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.collections.R.id.fab_add))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.collections.R.id.text_input_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.collections.R.id.edit_text_collection_name))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun collectionsFragment_shouldDisplayEmptyStateWhenNoCollections() {
        scenario = launchFragmentInHiltContainer<CollectionsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.collections.R.id.recycler_collections))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(com.koniukhov.cinecirclex.feature.collections.R.id.fab_add))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}