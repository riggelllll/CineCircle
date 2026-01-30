package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.CollectionDetails
import com.koniukhov.cinecirclex.core.domain.model.CollectionMedia
import com.koniukhov.cinecirclex.core.domain.repository.CollectionsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCollectionDetailsUseCaseTest {

    private lateinit var repository: CollectionsRepository
    private lateinit var useCase: GetCollectionDetailsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCollectionDetailsUseCase(repository)
    }

    @Test
    fun `invoke returns collection details from repository`() = runTest {
        val collectionId = 123
        val language = "en"
        val mockCollection = CollectionDetails(
            id = collectionId,
            name = "Test Collection",
            overview = "Collection overview",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            parts = listOf(
                CollectionMedia(
                    adult = false,
                    backdropPath = "/backdrop1.jpg",
                    id = 1,
                    title = "Movie 1",
                    originalLanguage = "en",
                    originalTitle = "Movie 1",
                    overview = "Overview 1",
                    posterPath = "/poster1.jpg",
                    mediaType = "movie",
                    genreIds = listOf(28, 12),
                    popularity = 100.0f,
                    releaseDate = "2023-01-15",
                    video = false,
                    voteAverage = 8.5f,
                    voteCount = 1000
                ),
                CollectionMedia(
                    adult = false,
                    backdropPath = "/backdrop2.jpg",
                    id = 2,
                    title = "Movie 2",
                    originalLanguage = "en",
                    originalTitle = "Movie 2",
                    overview = "Overview 2",
                    posterPath = "/poster2.jpg",
                    mediaType = "movie",
                    genreIds = listOf(28, 12),
                    popularity = 120.0f,
                    releaseDate = "2024-01-15",
                    video = false,
                    voteAverage = 9.0f,
                    voteCount = 1500
                )
            )
        )

        coEvery { repository.getCollectionDetails(collectionId, language) } returns mockCollection

        val result = useCase(collectionId, language)

        assertEquals(mockCollection, result)
        coVerify(exactly = 1) { repository.getCollectionDetails(collectionId, language) }
    }

    @Test
    fun `invoke returns collection with empty parts from repository`() = runTest {
        val collectionId = 123
        val language = "en"
        val emptyCollection = CollectionDetails(
            id = collectionId,
            name = "Empty Collection",
            overview = "Collection with no movies",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            parts = emptyList()
        )

        coEvery { repository.getCollectionDetails(collectionId, language) } returns emptyCollection

        val result = useCase(collectionId, language)

        assertEquals(emptyCollection, result)
        coVerify(exactly = 1) { repository.getCollectionDetails(collectionId, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val collectionId = 123
        val language = "en"

        coEvery { repository.getCollectionDetails(collectionId, language) } throws Exception("Network error")

        useCase(collectionId, language)
    }
}