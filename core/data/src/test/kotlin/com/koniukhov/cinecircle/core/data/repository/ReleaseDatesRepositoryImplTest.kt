package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.remote.RemoteReleaseDatesDataSourceImpl
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.network.model.ReleaseDatesDto
import com.koniukhov.cinecircle.core.network.model.ReleaseDatesResultDto
import com.koniukhov.cinecircle.core.network.model.ReleaseDatesResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReleaseDatesRepositoryImplTest {

    private lateinit var repository: ReleaseDatesRepositoryImpl
    private lateinit var remoteDataSource: RemoteReleaseDatesDataSourceImpl
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        networkStatusProvider = mockk()

        repository = ReleaseDatesRepositoryImpl(
            remoteReleaseDatesDataSourceImpl = remoteDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getMovieReleaseDates should return release dates when network is available`() = runTest {
        val releaseDate = ReleaseDatesDto(
            certification = "PG-13",
            languageCode = "en",
            releaseDate = "2023-01-01T00:00:00Z",
            type = 3,
            note = "Theatrical Release",
            descriptors = emptyList()
        )
        val releaseDateResult = ReleaseDatesResultDto(
            countryCode = "US",
            releaseDates = listOf(releaseDate)
        )
        val response = ReleaseDatesResponseDto(id = 100, results = listOf(releaseDateResult))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReleaseDates(any()) } returns response

        val result = repository.getMovieReleaseDates(movieId = 100)

        assertEquals(1, result.size)
        assertEquals("US", result[0].countryCode)
        assertEquals(1, result[0].releaseDates.size)
        assertEquals("PG-13", result[0].releaseDates[0].certification)
        coVerify { remoteDataSource.getMovieReleaseDates(100) }
    }

    @Test
    fun `getMovieReleaseDates should return empty list when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReleaseDates(any()) } throws Exception("Network error")

        val result = repository.getMovieReleaseDates(movieId = 100)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieReleaseDates should handle empty results`() = runTest {
        val response = ReleaseDatesResponseDto(id = 100, results = emptyList())

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReleaseDates(any()) } returns response

        val result = repository.getMovieReleaseDates(movieId = 100)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieReleaseDates should handle multiple countries`() = runTest {
        val usReleaseDate = ReleaseDatesDto(
            certification = "PG-13",
            languageCode = "en",
            releaseDate = "2023-01-01T00:00:00Z",
            type = 3,
            note = "Theatrical",
            descriptors = emptyList()
        )
        val ukReleaseDate = ReleaseDatesDto(
            certification = "12A",
            languageCode = "en",
            releaseDate = "2023-01-15T00:00:00Z",
            type = 3,
            note = "Theatrical",
            descriptors = emptyList()
        )
        val usResult = ReleaseDatesResultDto(
            countryCode = "US",
            releaseDates = listOf(usReleaseDate)
        )
        val ukResult = ReleaseDatesResultDto(
            countryCode = "GB",
            releaseDates = listOf(ukReleaseDate)
        )
        val response = ReleaseDatesResponseDto(id = 100, results = listOf(usResult, ukResult))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReleaseDates(any()) } returns response

        val result = repository.getMovieReleaseDates(movieId = 100)

        assertEquals(2, result.size)
        assertEquals("US", result[0].countryCode)
        assertEquals("GB", result[1].countryCode)
    }

    @Test
    fun `getMovieReleaseDates should handle multiple release dates per country`() = runTest {
        val releaseDate1 = ReleaseDatesDto(
            certification = "R",
            languageCode = "en",
            releaseDate = "2023-01-01T00:00:00Z",
            type = 1,
            note = "Premiere",
            descriptors = emptyList()
        )
        val releaseDate2 = ReleaseDatesDto(
            certification = "R",
            languageCode = "en",
            releaseDate = "2023-01-15T00:00:00Z",
            type = 3,
            note = "Theatrical",
            descriptors = emptyList()
        )
        val releaseDateResult = ReleaseDatesResultDto(
            countryCode = "US",
            releaseDates = listOf(releaseDate1, releaseDate2)
        )
        val response = ReleaseDatesResponseDto(id = 100, results = listOf(releaseDateResult))

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReleaseDates(any()) } returns response

        val result = repository.getMovieReleaseDates(movieId = 100)

        assertEquals(1, result.size)
        assertEquals(2, result[0].releaseDates.size)
    }
}