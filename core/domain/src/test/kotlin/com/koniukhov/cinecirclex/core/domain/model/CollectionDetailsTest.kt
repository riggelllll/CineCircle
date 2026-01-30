package com.koniukhov.cinecirclex.core.domain.model

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID
import org.junit.Assert.*
import org.junit.Test

class CollectionDetailsTest {

    @Test
    fun `exists should return true for valid collection ID`() {
        val collection = CollectionDetails(
            id = 100,
            name = "Marvel Cinematic Universe",
            overview = "All MCU movies",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            parts = emptyList()
        )

        assertTrue(collection.exists())
    }

    @Test
    fun `exists should return false for INVALID_ID`() {
        val collection = CollectionDetails(
            id = INVALID_ID,
            name = "",
            overview = "",
            posterPath = "",
            backdropPath = "",
            parts = emptyList()
        )

        assertFalse(collection.exists())
    }

    @Test
    fun `exists should return true for positive ID`() {
        val collection = CollectionDetails(
            id = 1,
            name = "Test Collection",
            overview = "Test",
            posterPath = "",
            backdropPath = "",
            parts = emptyList()
        )

        assertTrue(collection.exists())
    }

    @Test
    fun `exists should return false for negative ID`() {
        val collection = CollectionDetails(
            id = -1,
            name = "Test Collection",
            overview = "Test",
            posterPath = "",
            backdropPath = "",
            parts = emptyList()
        )

        assertFalse(collection.exists())
    }

    @Test
    fun `empty should create collection with INVALID_ID`() {
        val result = CollectionDetails.empty()

        assertEquals(INVALID_ID, result.id)
        assertFalse(result.exists())
    }

    @Test
    fun `empty should create collection with empty strings`() {
        val result = CollectionDetails.empty()

        assertEquals("", result.name)
        assertEquals("", result.overview)
        assertEquals("", result.posterPath)
        assertEquals("", result.backdropPath)
    }

    @Test
    fun `empty should create collection with empty parts list`() {
        val result = CollectionDetails.empty()

        assertTrue(result.parts.isEmpty())
    }

    @Test
    fun `collection with parts should preserve parts list`() {
        val part1 = CollectionMedia(
            adult = false,
            backdropPath = "/b1.jpg",
            id = 1,
            title = "Movie 1",
            originalLanguage = "en",
            originalTitle = "Movie 1",
            overview = "First movie",
            posterPath = "/p1.jpg",
            mediaType = "movie",
            genreIds = listOf(28),
            popularity = 100f,
            releaseDate = "2023-01-01",
            video = false,
            voteAverage = 8f,
            voteCount = 1000
        )
        val part2 = CollectionMedia(
            adult = false,
            backdropPath = "/b2.jpg",
            id = 2,
            title = "Movie 2",
            originalLanguage = "en",
            originalTitle = "Movie 2",
            overview = "Second movie",
            posterPath = "/p2.jpg",
            mediaType = "movie",
            genreIds = listOf(28),
            popularity = 150f,
            releaseDate = "2024-01-01",
            video = false,
            voteAverage = 8.5f,
            voteCount = 1500
        )

        val collection = CollectionDetails(
            id = 500,
            name = "Test Series",
            overview = "A test movie series",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            parts = listOf(part1, part2)
        )

        assertEquals(2, collection.parts.size)
        assertEquals("Movie 1", collection.parts[0].title)
        assertEquals("Movie 2", collection.parts[1].title)
    }

    @Test
    fun `exists should work independently of other fields`() {
        val collection = CollectionDetails(
            id = 42,
            name = "",
            overview = "",
            posterPath = "",
            backdropPath = "",
            parts = emptyList()
        )

        assertTrue(collection.exists())
    }

    @Test
    fun `collection with zero ID should still exist`() {
        val collection = CollectionDetails(
            id = 0,
            name = "Zero ID Collection",
            overview = "Test",
            posterPath = "",
            backdropPath = "",
            parts = emptyList()
        )

        assertTrue(collection.exists())
    }
}