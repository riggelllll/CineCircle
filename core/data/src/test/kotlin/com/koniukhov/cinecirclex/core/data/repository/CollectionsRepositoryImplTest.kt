package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.remote.RemoteCollectionsDataSourceImpl
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.network.model.CollectionDetailsDto
import com.koniukhov.cinecirclex.core.network.model.CollectionMediaDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CollectionsRepositoryImplTest {

    private lateinit var repository: CollectionsRepositoryImpl
    private lateinit var remoteDataSource: RemoteCollectionsDataSourceImpl
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        networkStatusProvider = mockk()

        repository = CollectionsRepositoryImpl(
            remoteDataSource = remoteDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getCollectionDetails should return collection details when network is available`() = runTest {
        val movie1 = CollectionMediaDto(
            id = 1,
            title = "Movie 1",
            posterPath = "/poster1.jpg",
            backdropPath = "/backdrop1.jpg",
            overview = "Overview 1",
            releaseDate = "2023-01-01",
            voteAverage = 7.5f,
            voteCount = 1000,
            genreIds = listOf(28),
            originalLanguage = "en",
            originalTitle = "Movie 1",
            popularity = 100.0f,
            adult = false,
            video = false,
            mediaType = "movie"
        )
        val movie2 = CollectionMediaDto(
            id = 2,
            title = "Movie 2",
            posterPath = "/poster2.jpg",
            backdropPath = "/backdrop2.jpg",
            overview = "Overview 2",
            releaseDate = "2024-01-01",
            voteAverage = 8.0f,
            voteCount = 1500,
            genreIds = listOf(28),
            originalLanguage = "en",
            originalTitle = "Movie 2",
            popularity = 150.0f,
            adult = false,
            video = false,
            mediaType = "movie"
        )
        val collectionDto = CollectionDetailsDto(
            id = 100,
            name = "Test Collection",
            overview = "Collection overview",
            posterPath = "/collection_poster.jpg",
            backdropPath = "/collection_backdrop.jpg",
            parts = listOf(movie1, movie2)
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getCollectionDetails(any(), any()) } returns collectionDto

        val result = repository.getCollectionDetails(collectionId = 100, language = "en")

        assertEquals(100, result.id)
        assertEquals("Test Collection", result.name)
        assertEquals("Collection overview", result.overview)
        assertEquals(2, result.parts.size)
        assertEquals("Movie 1", result.parts[0].title)
        assertEquals("Movie 2", result.parts[1].title)
        coVerify { remoteDataSource.getCollectionDetails(100, "en") }
    }

    @Test
    fun `getCollectionDetails should return empty collection when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getCollectionDetails(any(), any()) } throws Exception("Network error")

        val result = repository.getCollectionDetails(collectionId = 100, language = "en")

        assertEquals(-1, result.id)
        assertTrue(result.name.isEmpty())
        assertTrue(result.parts.isEmpty())
    }

    @Test
    fun `getCollectionDetails should handle collection with no parts`() = runTest {
        val collectionDto = CollectionDetailsDto(
            id = 100,
            name = "Empty Collection",
            overview = "No movies yet",
            posterPath = null,
            backdropPath = null,
            parts = emptyList()
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getCollectionDetails(any(), any()) } returns collectionDto

        val result = repository.getCollectionDetails(collectionId = 100, language = "en")

        assertEquals(100, result.id)
        assertEquals("Empty Collection", result.name)
        assertTrue(result.parts.isEmpty())
    }

    @Test
    fun `getCollectionDetails should handle collection with many parts`() = runTest {
        val movies = (1..10).map { index ->
            CollectionMediaDto(
                id = index,
                title = "Movie $index",
                posterPath = "/poster$index.jpg",
                backdropPath = "/backdrop$index.jpg",
                overview = "Overview $index",
                releaseDate = "202$index-01-01",
                voteAverage = 7.0f + index * 0.1f,
                voteCount = 1000 * index,
                genreIds = listOf(28, 12),
                originalLanguage = "en",
                originalTitle = "Movie $index",
                popularity = 100.0f * index,
                adult = false,
                video = false,
                mediaType = "movie"
            )
        }
        val collectionDto = CollectionDetailsDto(
            id = 200,
            name = "Large Collection",
            overview = "Collection with many movies",
            posterPath = "/large_poster.jpg",
            backdropPath = "/large_backdrop.jpg",
            parts = movies
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getCollectionDetails(any(), any()) } returns collectionDto

        val result = repository.getCollectionDetails(collectionId = 200, language = "en")

        assertEquals(200, result.id)
        assertEquals("Large Collection", result.name)
        assertEquals(10, result.parts.size)
        assertEquals("Movie 1", result.parts[0].title)
        assertEquals("Movie 10", result.parts[9].title)
    }

    @Test
    fun `getCollectionDetails should handle null poster and backdrop paths`() = runTest {
        val collectionDto = CollectionDetailsDto(
            id = 300,
            name = "Collection Without Images",
            overview = "No images",
            posterPath = null,
            backdropPath = null,
            parts = emptyList()
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getCollectionDetails(any(), any()) } returns collectionDto

        val result = repository.getCollectionDetails(collectionId = 300, language = "en")

        assertEquals(300, result.id)
        assertEquals("Collection Without Images", result.name)
        assertEquals("", result.posterPath)
        assertEquals("", result.backdropPath)
    }
}