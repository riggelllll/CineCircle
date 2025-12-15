package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.remote.RemoteReviewsDataSourceImpl
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.network.model.ReviewAuthorDto
import com.koniukhov.cinecircle.core.network.model.MediaReviewDto
import com.koniukhov.cinecircle.core.network.model.MovieReviewsResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReviewsRepositoryImplTest {

    private lateinit var repository: ReviewsRepositoryImpl
    private lateinit var remoteDataSource: RemoteReviewsDataSourceImpl
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Before
    fun setup() {
        remoteDataSource = mockk()
        networkStatusProvider = mockk()

        repository = ReviewsRepositoryImpl(
            remoteReviewsDataSourceImpl = remoteDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `getMovieReviews should return reviews when network is available`() = runTest {
        val authorDetails = ReviewAuthorDto(
            name = "John Doe",
            username = "johndoe",
            avatarPath = "/avatar.jpg",
            rating = "8.0"
        )
        val reviewDto = MediaReviewDto(
            id = "review1",
            author = "John Doe",
            authorDetails = authorDetails,
            content = "Great movie!",
            createdAt = "2023-01-01T00:00:00Z",
            updatedAt = "2023-01-02T00:00:00Z",
            url = "http://example.com/review1"
        )
        val response = MovieReviewsResponseDto(
            id = 100,
            page = 1,
            results = listOf(reviewDto),
            totalPages = 1,
            totalResults = 1
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReviews(any(), any(), any()) } returns response

        val result = repository.getMovieReviews(movieId = 100, page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("review1", result[0].id)
        assertEquals("John Doe", result[0].author)
        assertEquals("Great movie!", result[0].content)
        coVerify { remoteDataSource.getMovieReviews(100, 1, "en") }
    }

    @Test
    fun `getMovieReviews should return empty list when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReviews(any(), any(), any()) } throws Exception("Network error")

        val result = repository.getMovieReviews(movieId = 100, page = 1, language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesReviews should return reviews when network is available`() = runTest {
        val authorDetails = ReviewAuthorDto(
            name = "Jane Smith",
            username = "janesmith",
            avatarPath = "/avatar2.jpg",
            rating = "9.0"
        )
        val reviewDto = MediaReviewDto(
            id = "review2",
            author = "Jane Smith",
            authorDetails = authorDetails,
            content = "Amazing series!",
            createdAt = "2023-02-01T00:00:00Z",
            updatedAt = "2023-02-02T00:00:00Z",
            url = "http://example.com/review2"
        )
        val response = MovieReviewsResponseDto(
            id = 200,
            page = 1,
            results = listOf(reviewDto),
            totalPages = 1,
            totalResults = 1
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesReviews(any(), any(), any()) } returns response

        val result = repository.getTvSeriesReviews(tvSeriesId = 200, page = 1, language = "en")

        assertEquals(1, result.size)
        assertEquals("review2", result[0].id)
        assertEquals("Jane Smith", result[0].author)
        assertEquals("Amazing series!", result[0].content)
        coVerify { remoteDataSource.getTvSeriesReviews(200, 1, "en") }
    }

    @Test
    fun `getTvSeriesReviews should return empty list when network fails`() = runTest {
        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesReviews(any(), any(), any()) } throws Exception("Network error")

        val result = repository.getTvSeriesReviews(tvSeriesId = 200, page = 1, language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieReviews should handle empty results`() = runTest {
        val response = MovieReviewsResponseDto(
            id = 100,
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReviews(any(), any(), any()) } returns response

        val result = repository.getMovieReviews(movieId = 100, page = 1, language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesReviews should handle empty results`() = runTest {
        val response = MovieReviewsResponseDto(
            id = 200,
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getTvSeriesReviews(any(), any(), any()) } returns response

        val result = repository.getTvSeriesReviews(tvSeriesId = 200, page = 1, language = "en")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieReviews should handle multiple reviews`() = runTest {
        val review1 = MediaReviewDto(
            id = "review1",
            author = "Reviewer 1",
            authorDetails = ReviewAuthorDto("Reviewer 1", "reviewer1", null, "7.0"),
            content = "Good",
            createdAt = "2023-01-01T00:00:00Z",
            updatedAt = "2023-01-01T00:00:00Z",
            url = "http://example.com/review1"
        )
        val review2 = MediaReviewDto(
            id = "review2",
            author = "Reviewer 2",
            authorDetails = ReviewAuthorDto("Reviewer 2", "reviewer2", null, "9.0"),
            content = "Excellent",
            createdAt = "2023-01-02T00:00:00Z",
            updatedAt = "2023-01-02T00:00:00Z",
            url = "http://example.com/review2"
        )
        val response = MovieReviewsResponseDto(
            id = 100,
            page = 1,
            results = listOf(review1, review2),
            totalPages = 1,
            totalResults = 2
        )

        coEvery { networkStatusProvider.isNetworkAvailable() } returns true
        coEvery { remoteDataSource.getMovieReviews(any(), any(), any()) } returns response

        val result = repository.getMovieReviews(movieId = 100, page = 1, language = "en")

        assertEquals(2, result.size)
        assertEquals("review1", result[0].id)
        assertEquals("review2", result[1].id)
    }
}