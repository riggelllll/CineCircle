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
import org.junit.Before
import org.junit.Test

class GetTvSeriesReviewsUseCaseTest {

    private lateinit var repository: ReviewsRepository
    private lateinit var useCase: GetTvSeriesReviewsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesReviewsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns tv series reviews from repository`() = runTest {
        val tvSeriesId = 1396
        val page = 1
        val languageCode = "en"
        val expectedReviews = listOf(
            MediaReview(
                author = "John Doe",
                authorDetails = ReviewAuthor(
                    name = "John Doe",
                    username = "johndoe",
                    avatarPath = "/avatar1.jpg",
                    rating = "10"
                ),
                content = "Great show! Best TV series ever.",
                createdAt = "2021-01-15T10:30:00.000Z",
                id = "review1",
                updatedAt = "2021-01-15T10:30:00.000Z",
                url = "https://www.themoviedb.org/review/review1"
            ),
            MediaReview(
                author = "Jane Smith",
                authorDetails = ReviewAuthor(
                    name = "Jane Smith",
                    username = "janesmith",
                    avatarPath = "/avatar2.jpg",
                    rating = "9"
                ),
                content = "Amazing storytelling and acting.",
                createdAt = "2021-02-20T14:45:00.000Z",
                id = "review2",
                updatedAt = "2021-02-20T14:45:00.000Z",
                url = "https://www.themoviedb.org/review/review2"
            )
        )

        coEvery { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) } returns expectedReviews

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(expectedReviews, result)
        assertEquals(2, result.size)
        coVerify(exactly = 1) { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) }
    }

    @Test
    fun `invoke returns empty list when no reviews available`() = runTest {
        val tvSeriesId = 1396
        val page = 1
        val languageCode = "en"

        coEvery { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) } returns emptyList()

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(emptyList<MediaReview>(), result)
        coVerify(exactly = 1) { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 1396
        val page = 1
        val languageCode = "en"

        coEvery { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) } throws Exception("Network error")

        useCase(tvSeriesId, languageCode, page)
    }

    @Test
    fun `invoke handles different tv series ids correctly`() = runTest {
        val tvSeriesId = 1668
        val page = 1
        val languageCode = "en"
        val expectedReviews = listOf(
            MediaReview(
                author = "Bob Wilson",
                authorDetails = ReviewAuthor(
                    name = "Bob Wilson",
                    username = "bobwilson",
                    avatarPath = "/avatar3.jpg",
                    rating = "8"
                ),
                content = "Good show with interesting characters.",
                createdAt = "2021-03-10T09:20:00.000Z",
                id = "review3",
                updatedAt = "2021-03-10T09:20:00.000Z",
                url = "https://www.themoviedb.org/review/review3"
            )
        )

        coEvery { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) } returns expectedReviews

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(expectedReviews, result)
        coVerify(exactly = 1) { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) }
    }

    @Test
    fun `invoke handles different page numbers correctly`() = runTest {
        val tvSeriesId = 1396
        val page = 2
        val languageCode = "en"
        val expectedReviews = listOf(
            MediaReview(
                author = "Alice Johnson",
                authorDetails = ReviewAuthor(
                    name = "Alice Johnson",
                    username = "alicejohnson",
                    avatarPath = "/avatar5.jpg",
                    rating = "9"
                ),
                content = "Second page review content.",
                createdAt = "2021-05-12T11:00:00.000Z",
                id = "review5",
                updatedAt = "2021-05-12T11:00:00.000Z",
                url = "https://www.themoviedb.org/review/review5"
            )
        )

        coEvery { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) } returns expectedReviews

        val result = useCase(tvSeriesId, languageCode, page)

        assertEquals(expectedReviews, result)
        coVerify(exactly = 1) { repository.getTvSeriesReviews(tvSeriesId, page, languageCode) }
    }
}