package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.ContentRating
import com.koniukhov.cinecirclex.core.domain.repository.ContentRatingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTvSeriesContentRatingsUseCaseTest {

    private lateinit var repository: ContentRatingsRepository
    private lateinit var useCase: GetTvSeriesContentRatingsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesContentRatingsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns content ratings from repository`() = runTest {
        val tvSeriesId = 1396
        val expectedRatings = listOf(
            ContentRating(descriptors = emptyList(), countryCode = "US", rating = "TV-MA"),
            ContentRating(descriptors = emptyList(), countryCode = "GB", rating = "18"),
            ContentRating(descriptors = emptyList(), countryCode = "DE", rating = "16")
        )

        coEvery { repository.getTvSeriesContentRatings(tvSeriesId) } returns expectedRatings

        val result = useCase(tvSeriesId)

        assertEquals(expectedRatings, result)
        coVerify(exactly = 1) { repository.getTvSeriesContentRatings(tvSeriesId) }
    }

    @Test
    fun `invoke returns empty list when no ratings available`() = runTest {
        val tvSeriesId = 1396

        coEvery { repository.getTvSeriesContentRatings(tvSeriesId) } returns emptyList()

        val result = useCase(tvSeriesId)

        assertEquals(emptyList<ContentRating>(), result)
        coVerify(exactly = 1) { repository.getTvSeriesContentRatings(tvSeriesId) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 1396

        coEvery { repository.getTvSeriesContentRatings(tvSeriesId) } throws Exception("Network error")

        useCase(tvSeriesId)
    }

    @Test
    fun `invoke handles different tv series ids correctly`() = runTest {
        val tvSeriesId = 1668
        val expectedRatings = listOf(
            ContentRating(descriptors = emptyList(), countryCode = "US", rating = "TV-14"),
            ContentRating(descriptors = emptyList(), countryCode = "GB", rating = "12")
        )

        coEvery { repository.getTvSeriesContentRatings(tvSeriesId) } returns expectedRatings

        val result = useCase(tvSeriesId)

        assertEquals(expectedRatings, result)
        coVerify(exactly = 1) { repository.getTvSeriesContentRatings(tvSeriesId) }
    }

    @Test
    fun `invoke handles single rating correctly`() = runTest {
        val tvSeriesId = 1399
        val expectedRatings = listOf(
            ContentRating(descriptors = emptyList(), countryCode = "US", rating = "TV-MA")
        )

        coEvery { repository.getTvSeriesContentRatings(tvSeriesId) } returns expectedRatings

        val result = useCase(tvSeriesId)

        assertEquals(expectedRatings, result)
        assertEquals(1, result.size)
        coVerify(exactly = 1) { repository.getTvSeriesContentRatings(tvSeriesId) }
    }

    @Test
    fun `invoke handles multiple ratings from different countries`() = runTest {
        val tvSeriesId = 60059
        val expectedRatings = listOf(
            ContentRating(descriptors = emptyList(), countryCode = "US", rating = "TV-PG"),
            ContentRating(descriptors = emptyList(), countryCode = "GB", rating = "PG"),
            ContentRating(descriptors = emptyList(), countryCode = "DE", rating = "6"),
            ContentRating(descriptors = emptyList(), countryCode = "FR", rating = "10"),
            ContentRating(descriptors = emptyList(), countryCode = "RU", rating = "12+")
        )

        coEvery { repository.getTvSeriesContentRatings(tvSeriesId) } returns expectedRatings

        val result = useCase(tvSeriesId)

        assertEquals(expectedRatings, result)
        assertEquals(5, result.size)
        coVerify(exactly = 1) { repository.getTvSeriesContentRatings(tvSeriesId) }
    }
}