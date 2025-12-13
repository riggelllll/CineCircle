package com.koniukhov.cinecircle.feature.lists.viewmodel

import app.cash.turbine.test
import com.koniukhov.cinecircle.core.database.model.MediaListWithCount
import com.koniukhov.cinecircle.core.database.repository.MediaListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionsViewModelTest {

    private lateinit var viewModel: CollectionsViewModel
    private lateinit var mediaListRepository: MediaListRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mediaListRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load collections from repository`() = runTest(testDispatcher) {
        val collections = listOf(
            MediaListWithCount(id = 1L, name = "Favorites", itemCount = 5, isDefault = true),
            MediaListWithCount(id = 2L, name = "Watch Later", itemCount = 10, isDefault = false)
        )

        coEvery { mediaListRepository.getAllListsWithCount() } returns flowOf(collections)

        viewModel = CollectionsViewModel(mediaListRepository)
        advanceUntilIdle()

        viewModel.collections.test {
            val loadedCollections = awaitItem()
            assertEquals(2, loadedCollections.size)
            assertEquals("Favorites", loadedCollections[0].name)
            assertEquals(5, loadedCollections[0].itemCount)
            assertEquals("Watch Later", loadedCollections[1].name)
            assertEquals(10, loadedCollections[1].itemCount)
        }

        coVerify { mediaListRepository.getAllListsWithCount() }
    }

    @Test
    fun `init should handle empty collections list`() = runTest(testDispatcher) {
        coEvery { mediaListRepository.getAllListsWithCount() } returns flowOf(emptyList())

        viewModel = CollectionsViewModel(mediaListRepository)
        advanceUntilIdle()

        viewModel.collections.test {
            val collections = awaitItem()
            assertTrue(collections.isEmpty())
        }

        coVerify { mediaListRepository.getAllListsWithCount() }
    }

    @Test
    fun `createCollection should call repository to create new collection`() = runTest(testDispatcher) {
        val collectionName = "My New Collection"
        coEvery { mediaListRepository.getAllListsWithCount() } returns flowOf(emptyList())
        coEvery { mediaListRepository.createList(collectionName) } returns 1L

        viewModel = CollectionsViewModel(mediaListRepository)
        advanceUntilIdle()

        viewModel.createCollection(collectionName)
        advanceUntilIdle()

        coVerify { mediaListRepository.createList(collectionName) }
    }

    @Test
    fun `deleteCollection should call repository to delete collection`() = runTest(testDispatcher) {
        val collectionId = 1L
        coEvery { mediaListRepository.getAllListsWithCount() } returns flowOf(emptyList())
        coEvery { mediaListRepository.deleteList(collectionId) } returns Unit

        viewModel = CollectionsViewModel(mediaListRepository)
        advanceUntilIdle()

        viewModel.deleteCollection(collectionId)
        advanceUntilIdle()

        coVerify { mediaListRepository.deleteList(collectionId) }
    }

    @Test
    fun `createCollection should handle exceptions gracefully`() = runTest(testDispatcher) {
        val collectionName = "Test Collection"
        coEvery { mediaListRepository.getAllListsWithCount() } returns flowOf(emptyList())
        coEvery { mediaListRepository.createList(collectionName) } throws Exception("Database error")

        viewModel = CollectionsViewModel(mediaListRepository)
        advanceUntilIdle()

        viewModel.createCollection(collectionName)
        advanceUntilIdle()

        coVerify { mediaListRepository.createList(collectionName) }
    }

    @Test
    fun `deleteCollection should handle exceptions gracefully`() = runTest(testDispatcher) {
        val collectionId = 1L
        coEvery { mediaListRepository.getAllListsWithCount() } returns flowOf(emptyList())
        coEvery { mediaListRepository.deleteList(collectionId) } throws Exception("Delete failed")

        viewModel = CollectionsViewModel(mediaListRepository)
        advanceUntilIdle()

        viewModel.deleteCollection(collectionId)
        advanceUntilIdle()

        coVerify { mediaListRepository.deleteList(collectionId) }
    }

    @Test
    fun `collections flow should update when repository emits new data`() = runTest(testDispatcher) {
        val initialCollections = listOf(
            MediaListWithCount(id = 1L, name = "Collection 1", itemCount = 3, isDefault = false)
        )
        val updatedCollections = listOf(
            MediaListWithCount(id = 1L, name = "Collection 1", itemCount = 3, isDefault = false),
            MediaListWithCount(id = 2L, name = "Collection 2", itemCount = 7, isDefault = false)
        )

        coEvery { mediaListRepository.getAllListsWithCount() } returns flowOf(initialCollections, updatedCollections)

        viewModel = CollectionsViewModel(mediaListRepository)
        advanceUntilIdle()

        viewModel.collections.test {
            val collections = awaitItem()
            assertTrue(collections.isNotEmpty())
        }

        coVerify { mediaListRepository.getAllListsWithCount() }
    }
}