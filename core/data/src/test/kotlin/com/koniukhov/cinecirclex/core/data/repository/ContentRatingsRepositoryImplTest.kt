package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.remote.RemoteContentRatingsDataSourceImpl
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.network.model.ContentRatingDto
import com.koniukhov.cinecirclex.core.network.model.ContentRatingsResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ContentRatingsRepositoryImplTest {

    private lateinit var repository: ContentRatingsRepositoryImpl
    private lateinit var remoteDataSource: RemoteContentRatingsDataSourceImpl
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        networkStatusProvider = mockk()

        repository = ContentRatingsRepositoryImpl(
            remoteContentRatingsDataSourceImpl = remoteDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getTvSeriesContentRatings should return content ratings when network is available`() = runTest {
        val ratingDto = ContentRatingDto(
            countryCode = "US",
            rating = "TV-14",
            descriptors = emptyList()
        )
        val response = ContentRatingsResponseDto(id = 100, results = listOf(ratingDto))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesContentRatings(any()) } returns response

        val result = repository.getTvSeriesContentRatings(tvSeriesId = 100)

        assertEquals(1, result.size)
        assertEquals("US", result[0].countryCode)
        assertEquals("TV-14", result[0].rating)
        coVerify { remoteDataSource.getTvSeriesContentRatings(100) }
    }

    @Test
    fun `getTvSeriesContentRatings should return empty list when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesContentRatings(any()) } throws Exception("Network error")

        val result = repository.getTvSeriesContentRatings(tvSeriesId = 100)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesContentRatings should handle empty results`() = runTest {
        val response = ContentRatingsResponseDto(id = 100, results = emptyList())

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesContentRatings(any()) } returns response

        val result = repository.getTvSeriesContentRatings(tvSeriesId = 100)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesContentRatings should handle multiple countries`() = runTest {
        val usRating = ContentRatingDto(
            countryCode = "US",
            rating = "TV-MA",
            descriptors = emptyList()
        )
        val ukRating = ContentRatingDto(
            countryCode = "GB",
            rating = "18",
            descriptors = emptyList()
        )
        val response = ContentRatingsResponseDto(id = 100, results = listOf(usRating, ukRating))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesContentRatings(any()) } returns response

        val result = repository.getTvSeriesContentRatings(tvSeriesId = 100)

        assertEquals(2, result.size)
        assertEquals("US", result[0].countryCode)
        assertEquals("TV-MA", result[0].rating)
        assertEquals("GB", result[1].countryCode)
        assertEquals("18", result[1].rating)
    }

    @Test
    fun `getTvSeriesContentRatings should handle different rating types`() = runTest {
        val rating1 = ContentRatingDto(
            countryCode = "US",
            rating = "TV-G",
            descriptors = emptyList()
        )
        val rating2 = ContentRatingDto(
            countryCode = "DE",
            rating = "6",
            descriptors = emptyList()
        )
        val rating3 = ContentRatingDto(
            countryCode = "FR",
            rating = "10",
            descriptors = emptyList()
        )
        val response = ContentRatingsResponseDto(id = 100, results = listOf(rating1, rating2, rating3))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesContentRatings(any()) } returns response

        val result = repository.getTvSeriesContentRatings(tvSeriesId = 100)

        assertEquals(3, result.size)
        assertEquals("TV-G", result[0].rating)
        assertEquals("6", result[1].rating)
        assertEquals("10", result[2].rating)
    }
}