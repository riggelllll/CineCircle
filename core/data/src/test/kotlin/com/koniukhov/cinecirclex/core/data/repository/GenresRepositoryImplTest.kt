package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.remote.RemoteGenresDataSourceImpl
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.network.model.GenreDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GenresRepositoryImplTest {

    private lateinit var repository: GenresRepositoryImpl
    private lateinit var remoteDataSource: RemoteGenresDataSourceImpl
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        networkStatusProvider = mockk()

        repository = GenresRepositoryImpl(
            remoteGenresDataSourceImpl = remoteDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getMoviesGenreList should return movie genres when network is available`() = runTest {
        val genreDto1 = GenreDto(id = 28, name = "Action")
        val genreDto2 = GenreDto(id = 12, name = "Adventure")

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMoviesGenreList(any()) } returns listOf(genreDto1, genreDto2)

        val result = repository.getMoviesGenreList(language = "en")

        assertEquals(2, result.size)
        assertEquals(28, result[0].id)
        assertEquals("Action", result[0].name)
        assertEquals(12, result[1].id)
        assertEquals("Adventure", result[1].name)
        coVerify { remoteDataSource.getMoviesGenreList("en") }
    }

    @Test
    fun `getMoviesGenreList should return empty list when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMoviesGenreList(any()) } throws Exception("Network error")

        val result = repository.getMoviesGenreList(language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesGenreList should return tv series genres when network is available`() = runTest {
        val genreDto1 = GenreDto(id = 18, name = "Drama")
        val genreDto2 = GenreDto(id = 10765, name = "Sci-Fi & Fantasy")

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesGenreList(any()) } returns listOf(genreDto1, genreDto2)

        val result = repository.getTvSeriesGenreList(language = "en")

        assertEquals(2, result.size)
        assertEquals(18, result[0].id)
        assertEquals("Drama", result[0].name)
        assertEquals(10765, result[1].id)
        assertEquals("Sci-Fi & Fantasy", result[1].name)
        coVerify { remoteDataSource.getTvSeriesGenreList("en") }
    }

    @Test
    fun `getTvSeriesGenreList should return empty list when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesGenreList(any()) } throws Exception("Network error")

        val result = repository.getTvSeriesGenreList(language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMoviesGenreList should return empty list when response is empty`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMoviesGenreList(any()) } returns emptyList()

        val result = repository.getMoviesGenreList(language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesGenreList should return empty list when response is empty`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesGenreList(any()) } returns emptyList()

        val result = repository.getTvSeriesGenreList(language = "en")

        assertTrue(result.isEmpty())
    }
}