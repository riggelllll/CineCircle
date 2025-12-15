package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.remote.RemoteCreditsDataSourceImpl
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.network.model.CastMemberDto
import com.koniukhov.cinecircle.core.network.model.CrewMemberDto
import com.koniukhov.cinecircle.core.network.model.MediaCreditsDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreditsRepositoryImplTest {

    private lateinit var repository: CreditsRepositoryImpl
    private lateinit var remoteDataSource: RemoteCreditsDataSourceImpl
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        networkStatusProvider = mockk()

        repository = CreditsRepositoryImpl(
            remoteCreditsDataSourceImpl = remoteDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getMovieCredits should return credits when network is available`() = runTest {
        val castDto = CastMemberDto(
            id = 1,
            name = "Actor Name",
            profilePath = "/actor.jpg",
            character = "Main Character",
            order = 0,
            castId = 1,
            creditId = "credit1",
            adult = false,
            gender = 1,
            knownForDepartment = "Acting",
            originalName = "Actor Name",
            popularity = 10.0f
        )
        val crewDto = CrewMemberDto(
            id = 2,
            name = "Director Name",
            profilePath = "/director.jpg",
            job = "Director",
            department = "Directing",
            creditId = "credit2",
            adult = false,
            gender = 1,
            knownForDepartment = "Directing",
            originalName = "Director Name",
            popularity = 10.0f
        )
        val creditsDto = MediaCreditsDto(
            id = 100,
            cast = listOf(castDto),
            crew = listOf(crewDto)
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieCredits(any(), any()) } returns creditsDto

        val result = repository.getMovieCredits(movieId = 100, language = "en")

        assertEquals(1, result.cast.size)
        assertEquals("Actor Name", result.cast[0].name)
        assertEquals(1, result.crew.size)
        assertEquals("Director Name", result.crew[0].name)
        coVerify { remoteDataSource.getMovieCredits(100, "en") }
    }

    @Test
    fun `getMovieCredits should return empty credits when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieCredits(any(), any()) } throws Exception("Network error")

        val result = repository.getMovieCredits(movieId = 100, language = "en")

        assertTrue(result.cast.isEmpty())
        assertTrue(result.crew.isEmpty())
    }

    @Test
    fun `getTvSeriesCredits should return credits when network is available`() = runTest {
        val castDto = CastMemberDto(
            id = 3,
            name = "TV Actor Name",
            profilePath = "/tv_actor.jpg",
            character = "Lead Character",
            order = 0,
            castId = 3,
            creditId = "credit3",
            adult = false,
            gender = 2,
            knownForDepartment = "Acting",
            originalName = "TV Actor Name",
            popularity = 15.0f
        )
        val crewDto = CrewMemberDto(
            id = 4,
            name = "Showrunner Name",
            profilePath = "/showrunner.jpg",
            job = "Showrunner",
            department = "Production",
            creditId = "credit4",
            adult = false,
            gender = 1,
            knownForDepartment = "Production",
            originalName = "Showrunner Name",
            popularity = 12.0f
        )
        val creditsDto = MediaCreditsDto(
            id = 200,
            cast = listOf(castDto),
            crew = listOf(crewDto)
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesCredits(any(), any()) } returns creditsDto

        val result = repository.getTvSeriesCredits(tvSeriesId = 200, language = "en")

        assertEquals(1, result.cast.size)
        assertEquals("TV Actor Name", result.cast[0].name)
        assertEquals(1, result.crew.size)
        assertEquals("Showrunner Name", result.crew[0].name)
        coVerify { remoteDataSource.getTvSeriesCredits(200, "en") }
    }

    @Test
    fun `getTvSeriesCredits should return empty credits when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesCredits(any(), any()) } throws Exception("Network error")

        val result = repository.getTvSeriesCredits(tvSeriesId = 200, language = "en")

        assertTrue(result.cast.isEmpty())
        assertTrue(result.crew.isEmpty())
    }

    @Test
    fun `getMovieCredits should handle empty cast and crew`() = runTest {
        val creditsDto = MediaCreditsDto(
            id = 100,
            cast = emptyList(),
            crew = emptyList()
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieCredits(any(), any()) } returns creditsDto

        val result = repository.getMovieCredits(movieId = 100, language = "en")

        assertTrue(result.cast.isEmpty())
        assertTrue(result.crew.isEmpty())
    }

    @Test
    fun `getTvSeriesCredits should handle empty cast and crew`() = runTest {
        val creditsDto = MediaCreditsDto(
            id = 200,
            cast = emptyList(),
            crew = emptyList()
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesCredits(any(), any()) } returns creditsDto

        val result = repository.getTvSeriesCredits(tvSeriesId = 200, language = "en")

        assertTrue(result.cast.isEmpty())
        assertTrue(result.crew.isEmpty())
    }
}