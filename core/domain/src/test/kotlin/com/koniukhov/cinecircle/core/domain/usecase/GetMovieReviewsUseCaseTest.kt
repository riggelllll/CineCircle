package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.domain.model.ReviewAuthor
import com.koniukhov.cinecircle.core.domain.repository.ReviewsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMovieReviewsUseCaseTest {

    private lateinit var repository: ReviewsRepository
    private lateinit var useCase: GetMovieReviewsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieReviewsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke calls repository with correct parameters and returns result`() = runTest {
        val movieId = 123
        val page = 1
        val language = "en"
        val expectedReviews = listOf(
            MediaReview(
                author = "John Doe",
                authorDetails = ReviewAuthor.empty(),
                content = "Great movie!",
                createdAt = "2024-01-01",
                id = "review1",
                updatedAt = "2024-01-01",
                url = "http://example.com/review1"
            )
        )
        coEvery { repository.getMovieReviews(movieId, page, language) } returns expectedReviews

        val result = useCase(movieId, page, language)

        assertNotNull(result)
        assertEquals(expectedReviews, result)
        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].author)
        coVerify(exactly = 1) { repository.getMovieReviews(movieId, page, language) }
    }

    @Test
    fun `invoke propagates exception from repository`() = runTest {
        val movieId = 123
        val page = 1
        val language = "en"
        val exception = RuntimeException("Network error")
        coEvery { repository.getMovieReviews(movieId, page, language) } throws exception

        try {
            useCase(movieId, page, language)
            throw AssertionError("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }

        coVerify { repository.getMovieReviews(movieId, page, language) }
    }

    @Test
    fun `invoke returns empty list when no reviews available`() = runTest {
        val movieId = 999
        val page = 1
        val language = "en"
        val emptyList = emptyList<MediaReview>()
        coEvery { repository.getMovieReviews(movieId, page, language) } returns emptyList

        val result = useCase(movieId, page, language)

        assertTrue(result.isEmpty())
        coVerify { repository.getMovieReviews(movieId, page, language) }
    }

    @Test
    fun `invoke with different page numbers returns different results`() = runTest {
        val movieId = 123
        val page1 = 1
        val page2 = 2
        val language = "en"
        val reviews1 = listOf(MediaReview("Author1", ReviewAuthor.empty(), "Content1", "", "id1", "", ""))
        val reviews2 = listOf(MediaReview("Author2", ReviewAuthor.empty(), "Content2", "", "id2", "", ""))
        coEvery { repository.getMovieReviews(movieId, page1, language) } returns reviews1
        coEvery { repository.getMovieReviews(movieId, page2, language) } returns reviews2

        val result1 = useCase(movieId, page1, language)
        val result2 = useCase(movieId, page2, language)

        assertEquals("Author1", result1[0].author)
        assertEquals("Author2", result2[0].author)
        coVerify { repository.getMovieReviews(movieId, page1, language) }
        coVerify { repository.getMovieReviews(movieId, page2, language) }
    }

    @Test
    fun `invoke with page zero still calls repository`() = runTest {
        val movieId = 123
        val page = 0
        val language = "en"
        val expectedReviews = emptyList<MediaReview>()
        coEvery { repository.getMovieReviews(movieId, page, language) } returns expectedReviews

        val result = useCase(movieId, page, language)

        assertNotNull(result)
        coVerify { repository.getMovieReviews(movieId, page, language) }
    }
}