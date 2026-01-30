package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.Movie
import com.koniukhov.cinecirclex.core.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetFilteredMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetFilteredMoviesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetFilteredMoviesUseCase(repository)
    }

    @Test
    fun `invoke returns filtered movies with all parameters from repository`() = runTest {
        val page = 1
        val language = "en"
        val sortBy = "popularity.desc"
        val year = 2023
        val releaseDateGte = "2023-01-01"
        val releaseDateLte = "2023-12-31"
        val minVoteAverage = 7.0f
        val maxVoteAverage = 10.0f
        val minVoteCount = 100
        val maxVoteCount = 10000
        val withOriginCountry = "US"
        val withOriginalLanguage = "en"
        val withGenres = "28,12"
        val withoutGenres = "27"

        val mockMovies = listOf(
            Movie(
                id = 1,
                title = "Test Movie 1",
                overview = "Overview 1",
                posterPath = "/poster1.jpg",
                backdropPath = "/backdrop1.jpg",
                releaseDate = "2023-06-15",
                voteAverage = 8.5f,
                voteCount = 1000,
                originalLanguage = "en",
                originalTitle = "Test Movie 1",
                popularity = 100.0f,
                adult = false,
                genreIds = listOf(28, 12),
                video = false
            )
        )

        coEvery {
            repository.getFilteredMovies(
                page = page,
                language = language,
                sortBy = sortBy,
                year = year,
                releaseDateGte = releaseDateGte,
                releaseDateLte = releaseDateLte,
                minVoteAverage = minVoteAverage,
                maxVoteAverage = maxVoteAverage,
                minVoteCount = minVoteCount,
                maxVoteCount = maxVoteCount,
                withOriginCountry = withOriginCountry,
                withOriginalLanguage = withOriginalLanguage,
                withGenres = withGenres,
                withoutGenres = withoutGenres
            )
        } returns mockMovies

        val result = useCase(
            page = page,
            language = language,
            sortBy = sortBy,
            year = year,
            releaseDateGte = releaseDateGte,
            releaseDateLte = releaseDateLte,
            minVoteAverage = minVoteAverage,
            maxVoteAverage = maxVoteAverage,
            minVoteCount = minVoteCount,
            maxVoteCount = maxVoteCount,
            withOriginCountry = withOriginCountry,
            withOriginalLanguage = withOriginalLanguage,
            withGenres = withGenres,
            withoutGenres = withoutGenres
        )

        assertEquals(mockMovies, result)
        coVerify(exactly = 1) {
            repository.getFilteredMovies(
                page = page,
                language = language,
                sortBy = sortBy,
                year = year,
                releaseDateGte = releaseDateGte,
                releaseDateLte = releaseDateLte,
                minVoteAverage = minVoteAverage,
                maxVoteAverage = maxVoteAverage,
                minVoteCount = minVoteCount,
                maxVoteCount = maxVoteCount,
                withOriginCountry = withOriginCountry,
                withOriginalLanguage = withOriginalLanguage,
                withGenres = withGenres,
                withoutGenres = withoutGenres
            )
        }
    }

    @Test
    fun `invoke returns filtered movies with minimal parameters from repository`() = runTest {
        val page = 1
        val language = "en"
        val sortBy = "popularity.desc"

        val mockMovies = emptyList<Movie>()

        coEvery {
            repository.getFilteredMovies(
                page = page,
                language = language,
                sortBy = sortBy,
                year = null,
                releaseDateGte = null,
                releaseDateLte = null,
                minVoteAverage = null,
                maxVoteAverage = null,
                minVoteCount = null,
                maxVoteCount = null,
                withOriginCountry = null,
                withOriginalLanguage = null,
                withGenres = null,
                withoutGenres = null
            )
        } returns mockMovies

        val result = useCase(
            page = page,
            language = language,
            sortBy = sortBy
        )

        assertEquals(mockMovies, result)
        coVerify(exactly = 1) {
            repository.getFilteredMovies(
                page = page,
                language = language,
                sortBy = sortBy,
                year = null,
                releaseDateGte = null,
                releaseDateLte = null,
                minVoteAverage = null,
                maxVoteAverage = null,
                minVoteCount = null,
                maxVoteCount = null,
                withOriginCountry = null,
                withOriginalLanguage = null,
                withGenres = null,
                withoutGenres = null
            )
        }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val page = 1
        val language = "en"
        val sortBy = "popularity.desc"

        coEvery {
            repository.getFilteredMovies(
                page = page,
                language = language,
                sortBy = sortBy,
                year = null,
                releaseDateGte = null,
                releaseDateLte = null,
                minVoteAverage = null,
                maxVoteAverage = null,
                minVoteCount = null,
                maxVoteCount = null,
                withOriginCountry = null,
                withOriginalLanguage = null,
                withGenres = null,
                withoutGenres = null
            )
        } throws Exception("Network error")

        useCase(
            page = page,
            language = language,
            sortBy = sortBy
        )
    }
}