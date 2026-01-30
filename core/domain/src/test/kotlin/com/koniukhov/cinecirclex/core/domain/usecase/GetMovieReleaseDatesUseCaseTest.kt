package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.ReleaseDate
import com.koniukhov.cinecirclex.core.domain.model.ReleaseDateResult
import com.koniukhov.cinecirclex.core.domain.repository.ReleaseDatesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetMovieReleaseDatesUseCaseTest {

    private lateinit var repository: ReleaseDatesRepository
    private lateinit var useCase: GetMovieReleaseDatesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetMovieReleaseDatesUseCase(repository)
    }

    @Test
    fun `invoke returns movie release dates from repository`() = runTest {
        val movieId = 123
        val mockReleaseDates = listOf(
            ReleaseDateResult(
                countryCode = "US",
                releaseDates = listOf(
                    ReleaseDate(
                        certification = "PG-13",
                        releaseDate = "2023-06-15T00:00:00.000Z",
                        releaseType = 3,
                        note = "Theatrical release",
                        descriptors = null,
                        languageCode = null
                    )
                )
            ),
            ReleaseDateResult(
                countryCode = "GB",
                releaseDates = listOf(
                    ReleaseDate(
                        certification = "12A",
                        releaseDate = "2023-06-16T00:00:00.000Z",
                        releaseType = 3,
                        note = "",
                        descriptors = null,
                        languageCode = null
                    )
                )
            )
        )

        coEvery { repository.getMovieReleaseDates(movieId) } returns mockReleaseDates

        val result = useCase(movieId)

        assertEquals(mockReleaseDates, result)
        coVerify(exactly = 1) { repository.getMovieReleaseDates(movieId) }
    }

    @Test
    fun `invoke returns empty release dates when repository returns empty list`() = runTest {
        val movieId = 123
        val emptyReleaseDates = emptyList<ReleaseDateResult>()

        coEvery { repository.getMovieReleaseDates(movieId) } returns emptyReleaseDates

        val result = useCase(movieId)

        assertEquals(emptyReleaseDates, result)
        coVerify(exactly = 1) { repository.getMovieReleaseDates(movieId) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val movieId = 123

        coEvery { repository.getMovieReleaseDates(movieId) } throws Exception("Network error")

        useCase(movieId)
    }
}