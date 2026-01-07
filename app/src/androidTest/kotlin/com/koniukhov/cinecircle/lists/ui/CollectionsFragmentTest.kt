package com.koniukhov.cinecircle.lists.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.feature.lists.ui.CollectionsFragment
import com.koniukhov.cinecircle.util.HiltTestActivity
import com.koniukhov.cinecircle.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.koniukhov.cinecircle.feature.collections.R as collectionsR

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

        onView(withId(collectionsR.id.recycler_collections))
            .check(matches(isDisplayed()))
    }

    @Test
    fun collectionsFragment_shouldDisplayAddCollectionFab() {
        scenario = launchFragmentInHiltContainer<CollectionsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(collectionsR.id.fab_add))
            .check(matches(isDisplayed()))
    }

    @Test
    fun collectionsFragment_shouldClickAddCollectionFab() {
        scenario = launchFragmentInHiltContainer<CollectionsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(collectionsR.id.fab_add))
            .perform(click())

        onView(withId(collectionsR.id.text_input_layout))
            .check(matches(isDisplayed()))

        onView(withId(collectionsR.id.edit_text_collection_name))
            .check(matches(isDisplayed()))
    }

    @Test
    fun collectionsFragment_shouldDisplayEmptyStateWhenNoCollections() {
        scenario = launchFragmentInHiltContainer<CollectionsFragment>(
            themeResId = R.style.Theme_CineCircle
        )

        onView(withId(collectionsR.id.recycler_collections))
            .check(matches(isDisplayed()))

        onView(withId(collectionsR.id.fab_add))
            .check(matches(isDisplayed()))
    }
}