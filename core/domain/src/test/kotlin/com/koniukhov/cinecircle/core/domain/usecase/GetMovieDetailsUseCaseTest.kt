package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Language
import com.koniukhov.cinecircle.core.domain.model.MovieCollection
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.model.ProductionCompany
import com.koniukhov.cinecircle.core.domain.model.ProductionCountry
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMovieDetailsUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieDetailsUseCase(repository)
    }

    @Test
    fun `invoke returns movie details from repository`() = runTest {
        val movieId = 278
        val language = "en"
        val expectedDetails = MovieDetails(
            adult = false,
            backdropPath = "/backdrop.jpg",
            belongsToCollection = MovieCollection.empty(),
            budget = 25000000,
            genres = listOf(
                Genre(id = 18, name = "Drama"),
                Genre(id = 80, name = "Crime")
            ),
            homePage = "https://example.com",
            id = movieId,
            imdbId = "tt0111161",
            originalLanguage = "en",
            originalTitle = "The Shawshank Redemption",
            overview = "Two imprisoned men bond over years",
            popularity = 95.5f,
            posterPath = "/poster.jpg",
            productionCompanies = listOf(
                ProductionCompany(
                    id = 1,
                    logoPath = "/logo.png",
                    name = "Castle Rock Entertainment",
                    originCountry = "US"
                )
            ),
            productionCountries = listOf(
                ProductionCountry(
                    countryCode = "US",
                    name = "United States of America"
                )
            ),
            releaseDate = "1994-09-23",
            revenue = 28341469,
            runtime = 142,
            spokenLanguages = listOf(
                Language(
                    languageCode = "en",
                    englishName = "English",
                    name = "English"
                )
            ),
            status = "Released",
            tagline = "Fear can hold you prisoner. Hope can set you free.",
            title = "The Shawshank Redemption",
            video = false,
            voteAverage = 8.7f,
            voteCount = 24000
        )

        coEvery { repository.getMovieDetails(movieId, language) } returns expectedDetails

        val result = useCase(movieId, language)

        assertEquals(expectedDetails, result)
        assertEquals(movieId, result.id)
        assertEquals("The Shawshank Redemption", result.title)
        coVerify(exactly = 1) { repository.getMovieDetails(movieId, language) }
    }

    @Test
    fun `invoke returns empty movie details when repository returns empty`() = runTest {
        val movieId = 0
        val language = "en"
        val emptyDetails = MovieDetails.empty()

        coEvery { repository.getMovieDetails(movieId, language) } returns emptyDetails

        val result = useCase(movieId, language)

        assertEquals(emptyDetails, result)
        coVerify(exactly = 1) { repository.getMovieDetails(movieId, language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val movieId = 278
        val language = "en"

        coEvery { repository.getMovieDetails(movieId, language) } throws Exception("Network error")

        useCase(movieId, language)
    }

    @Test
    fun `invoke with different movie IDs calls repository correctly`() = runTest {
        val movieId1 = 278
        val movieId2 = 238
        val language = "en"
        val details = MovieDetails.empty()

        coEvery { repository.getMovieDetails(any(), any()) } returns details

        useCase(movieId1, language)
        useCase(movieId2, language)

        coVerify(exactly = 1) { repository.getMovieDetails(movieId1, language) }
        coVerify(exactly = 1) { repository.getMovieDetails(movieId2, language) }
    }

    @Test
    fun `invoke with different languages calls repository correctly`() = runTest {
        val movieId = 278
        val languageEn = "en"
        val languageEs = "es"
        val details = MovieDetails.empty()

        coEvery { repository.getMovieDetails(any(), any()) } returns details

        useCase(movieId, languageEn)
        useCase(movieId, languageEs)

        coVerify(exactly = 1) { repository.getMovieDetails(movieId, languageEn) }
        coVerify(exactly = 1) { repository.getMovieDetails(movieId, languageEs) }
    }

    @Test
    fun `invoke verifies movie details structure`() = runTest {
        val movieId = 278
        val language = "en"
        val details = MovieDetails(
            adult = false,
            backdropPath = "/backdrop.jpg",
            belongsToCollection = MovieCollection.empty(),
            budget = 1000000,
            genres = listOf(Genre(id = 1, name = "Action")),
            homePage = "",
            id = movieId,
            imdbId = "tt12345",
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Overview text",
            popularity = 50.0f,
            posterPath = "/poster.jpg",
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            releaseDate = "2024-01-01",
            revenue = 5000000,
            runtime = 120,
            spokenLanguages = emptyList(),
            status = "Released",
            tagline = "Tagline",
            title = "Movie Title",
            video = false,
            voteAverage = 7.5f,
            voteCount = 100
        )

        coEvery { repository.getMovieDetails(movieId, language) } returns details

        val result = useCase(movieId, language)

        assertEquals(movieId, result.id)
        assertEquals(1000000, result.budget)
        assertEquals(5000000, result.revenue)
        assertEquals(120, result.runtime)
        assertEquals("Movie Title", result.title)
        assertEquals(1, result.genres.size)
        assertEquals("Action", result.genres[0].name)
    }
}