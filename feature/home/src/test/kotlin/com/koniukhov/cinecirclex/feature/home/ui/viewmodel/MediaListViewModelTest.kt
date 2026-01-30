package com.koniukhov.cinecirclex.feature.home.ui.viewmodel

import com.koniukhov.cinecirclex.core.common.model.MediaListType
import com.koniukhov.cinecirclex.feature.home.repository.MediaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MediaListViewModelTest {

    private lateinit var viewModel: MediaListViewModel
    private lateinit var mediaRepository: MediaRepository

    private val testDispatcher = StandardTestDispatcher()
    private val languageCode = "en"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mediaRepository = mockk(relaxed = true)

        viewModel = MediaListViewModel(
            repository = mediaRepository,
            languageCode = languageCode
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMedia should set loading state and then load media successfully`() = runTest(testDispatcher) {
        val listType = MediaListType.TRENDING_MOVIES

        viewModel.loadMedia(listType)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.mediaFlow)

        coVerify { mediaRepository.getMediaFlow(listType, languageCode, null) }
    }

    @Test
    fun `loadMedia with genre should load media with genre filter`() = runTest(testDispatcher) {
        val listType = MediaListType.MOVIES_BY_GENRE
        val genreId = 28

        viewModel.loadMedia(listType, genreId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.mediaFlow)

        coVerify { mediaRepository.getMediaFlow(listType, languageCode, genreId) }
    }

    @Test
    fun `loadMedia should handle error and update state`() = runTest(testDispatcher) {
        val listType = MediaListType.POPULAR_MOVIES
        val errorMessage = "Network error"

        coEvery {
            mediaRepository.getMediaFlow(listType, languageCode, null)
        } throws Exception(errorMessage)

        viewModel.loadMedia(listType)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertNull(state.mediaFlow)

        coVerify { mediaRepository.getMediaFlow(listType, languageCode, null) }
    }

    @Test
    fun `loadMedia for TV series should work correctly`() = runTest(testDispatcher) {
        val listType = MediaListType.TRENDING_TV_SERIES

        viewModel.loadMedia(listType)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.mediaFlow)

        coVerify { mediaRepository.getMediaFlow(listType, languageCode, null) }
    }

    @Test
    fun `loadMedia multiple times should update state correctly`() = runTest(testDispatcher) {
        val listType1 = MediaListType.TRENDING_MOVIES
        val listType2 = MediaListType.POPULAR_MOVIES

        viewModel.loadMedia(listType1)
        advanceUntilIdle()

        viewModel.loadMedia(listType2)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.mediaFlow)

        coVerify { mediaRepository.getMediaFlow(listType1, languageCode, null) }
        coVerify { mediaRepository.getMediaFlow(listType2, languageCode, null) }
    }

    @Test
    fun `loadMedia should clear previous error on new load`() = runTest(testDispatcher) {
        val listType = MediaListType.POPULAR_MOVIES

        coEvery {
            mediaRepository.getMediaFlow(listType, languageCode, null)
        } throws Exception("Network error")

        viewModel.loadMedia(listType)
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)

        coEvery {
            mediaRepository.getMediaFlow(listType, languageCode, null)
        } answers { callOriginal() }

        viewModel.loadMedia(listType)
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.mediaFlow)
    }
}