package com.koniukhov.cinecirclex.core.data.mapper

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID
import com.koniukhov.cinecirclex.core.network.model.CreatorDto
import com.koniukhov.cinecirclex.core.network.model.CrewMemberDto
import com.koniukhov.cinecirclex.core.network.model.GenreDto
import com.koniukhov.cinecirclex.core.network.model.LanguageDto
import com.koniukhov.cinecirclex.core.network.model.MovieCollectionDto
import com.koniukhov.cinecirclex.core.network.model.MovieDetailsDto
import com.koniukhov.cinecirclex.core.network.model.NetworkDto
import com.koniukhov.cinecirclex.core.network.model.ProductionCompanyDto
import com.koniukhov.cinecirclex.core.network.model.ProductionCountryDto
import com.koniukhov.cinecirclex.core.network.model.ReleaseDatesDto
import com.koniukhov.cinecirclex.core.network.model.ReleaseDatesResultDto
import com.koniukhov.cinecirclex.core.network.model.TvEpisodeDetailsDto
import com.koniukhov.cinecirclex.core.network.model.TvSeasonDetailsDto
import com.koniukhov.cinecirclex.core.network.model.TvSeriesDetailsDto
import org.junit.Assert.*
import org.junit.Test

class MediaMapperComplexTest {

    @Test
    fun `MovieDetailsDto toDomain should map all fields correctly`() {
        val genreDto = GenreDto(id = 28, name = "Action")
        val companyDto = ProductionCompanyDto(
            id = 1,
            logoPath = "/logo.jpg",
            name = "Warner",
            originCountry = "US"
        )
        val countryDto = ProductionCountryDto(countryCode = "US", name = "United States")
        val languageDto =
            LanguageDto(englishName = "English", languageCode = "en", name = "English")
        val collectionDto = MovieCollectionDto(
            id = 10,
            name = "Collection",
            posterPath = "/p.jpg",
            backdropPath = "/b.jpg"
        )

        val dto = MovieDetailsDto(
            adult = false,
            backdropPath = "/backdrop.jpg",
            belongsToCollection = collectionDto,
            budget = 1000000,
            genres = listOf(genreDto),
            homePage = "http://movie.com",
            id = 123,
            imdbId = "tt1234567",
            originalLanguage = "en",
            originalTitle = "Original",
            overview = "Overview",
            popularity = 100f,
            posterPath = "/poster.jpg",
            productionCompanies = listOf(companyDto),
            productionCountries = listOf(countryDto),
            releaseDate = "2023-01-01",
            revenue = 2000000,
            runtime = 120,
            spokenLanguages = listOf(languageDto),
            status = "Released",
            tagline = "Tagline",
            title = "Title",
            video = false,
            voteAverage = 8.5f,
            voteCount = 1000
        )

        val result = dto.toDomain()

        assertEquals(123, result.id)
        assertEquals("Title", result.title)
        assertEquals(1000000, result.budget)
        assertEquals(2000000L, result.revenue)
        assertEquals(120, result.runtime)
        assertEquals(1, result.genres.size)
        assertEquals("Action", result.genres[0].name)
        assertEquals(1, result.productionCompanies.size)
        assertEquals("Warner", result.productionCompanies[0].name)
        assertEquals(10, result.belongsToCollection.id)
        assertEquals("Collection", result.belongsToCollection.name)
    }

    @Test
    fun `MovieDetailsDto toDomain should handle all null values`() {
        val dto = MovieDetailsDto(
            adult = null,
            backdropPath = null,
            belongsToCollection = null,
            budget = null,
            genres = null,
            homePage = null,
            id = null,
            imdbId = null,
            originalLanguage = null,
            originalTitle = null,
            overview = null,
            popularity = null,
            posterPath = null,
            productionCompanies = null,
            productionCountries = null,
            releaseDate = null,
            revenue = null,
            runtime = null,
            spokenLanguages = null,
            status = null,
            tagline = null,
            title = null,
            video = null,
            voteAverage = null,
            voteCount = null
        )

        val result = dto.toDomain()

        assertEquals(INVALID_ID, result.id)
        assertEquals("", result.title)
        assertEquals(0, result.budget)
        assertEquals(0L, result.revenue)
        assertEquals(0, result.runtime)
        assertTrue(result.genres.isEmpty())
        assertTrue(result.productionCompanies.isEmpty())
        assertTrue(result.productionCountries.isEmpty())
        assertTrue(result.spokenLanguages.isEmpty())
        assertEquals(INVALID_ID, result.belongsToCollection.id)
    }

    @Test
    fun `TvSeriesDetailsDto toDomain should map all fields correctly`() {
        val genreDto = GenreDto(id = 18, name = "Drama")
        val creatorDto = CreatorDto(
            id = 1,
            creditId = "c1",
            name = "Creator",
            gender = 1,
            profilePath = "/c.jpg"
        )
        val networkDto =
            NetworkDto(name = "HBO", id = 1, logoPath = "/hbo.jpg", originCountry = "US")
        val episodeDto = TvEpisodeDetailsDto(
            airDate = "2023-01-01", crew = emptyList(), episodeNumber = 1,
            guestStars = emptyList(), name = "Episode 1", overview = "Overview",
            id = 1, productionCode = "P1", runtime = 50, seasonNumber = 1,
            stillPath = "/still.jpg", voteAverage = 8f, voteCount = 100
        )
        val seasonDto = TvSeasonDetailsDto(
            _id = "s1", airDate = "2023-01-01", episodes = listOf(episodeDto),
            name = "Season 1", overview = "Season overview", id = 1,
            posterPath = "/season.jpg", seasonNumber = 1, voteAverage = 8.5f
        )

        val dto = TvSeriesDetailsDto(
            adult = false,
            backdropPath = "/backdrop.jpg",
            createdBy = listOf(creatorDto),
            episodeRunTime = listOf(50, 60),
            firstAirDate = "2023-01-01",
            genres = listOf(genreDto),
            homepage = "http://series.com",
            id = 456,
            inProduction = true,
            languages = listOf("en"),
            lastAirDate = "2023-12-31",
            lastEpisodeToAir = episodeDto,
            name = "Series Name",
            nextEpisodeToAir = null,
            networks = listOf(networkDto),
            numberOfEpisodes = 10,
            numberOfSeasons = 1,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Original Name",
            overview = "Overview",
            popularity = 200f,
            posterPath = "/poster.jpg",
            productionCompanies = emptyList(),
            seasons = listOf(seasonDto),
            spokenLanguages = emptyList(),
            status = "Returning Series",
            tagline = "Tagline",
            type = "Scripted",
            voteAverage = 9f,
            voteCount = 2000,
            productionCountries = emptyList()
        )

        val result = dto.toDomain()

        assertEquals(456, result.id)
        assertEquals("Series Name", result.name)
        assertEquals(true, result.inProduction)
        assertEquals(10, result.numberOfEpisodes)
        assertEquals(1, result.numberOfSeasons)
        assertEquals(1, result.genres.size)
        assertEquals("Drama", result.genres[0].name)
        assertEquals(1, result.createdBy.size)
        assertEquals("Creator", result.createdBy[0].name)
        assertEquals(1, result.networks.size)
        assertEquals("HBO", result.networks[0].name)
        assertEquals("Episode 1", result.lastEpisodeToAir.name)
    }

    @Test
    fun `TvSeriesDetailsDto toDomain should handle null values`() {
        val dto = TvSeriesDetailsDto(
            adult = null,
            backdropPath = null,
            createdBy = null,
            episodeRunTime = null,
            firstAirDate = null,
            genres = null,
            homepage = null,
            id = null,
            inProduction = null,
            languages = null,
            lastAirDate = null,
            lastEpisodeToAir = null,
            name = null,
            nextEpisodeToAir = null,
            networks = null,
            numberOfEpisodes = null,
            numberOfSeasons = null,
            originCountry = null,
            originalLanguage = null,
            originalName = null,
            overview = null,
            popularity = null,
            posterPath = null,
            productionCompanies = null,
            seasons = null,
            spokenLanguages = null,
            status = null,
            tagline = null,
            type = null,
            voteAverage = null,
            voteCount = null,
            productionCountries = null
        )

        val result = dto.toDomain()

        assertEquals(INVALID_ID, result.id)
        assertEquals("", result.name)
        assertEquals(false, result.inProduction)
        assertEquals(0, result.numberOfEpisodes)
        assertEquals(0, result.numberOfSeasons)
        assertTrue(result.genres.isEmpty())
        assertTrue(result.createdBy.isEmpty())
        assertTrue(result.networks.isEmpty())
        assertTrue(result.seasons.isEmpty())
    }

    @Test
    fun `TvSeasonDetailsDto toDomain should map correctly`() {
        val episodeDto = TvEpisodeDetailsDto(
            airDate = "2023-01-01", crew = emptyList(), episodeNumber = 1,
            guestStars = emptyList(), name = "Pilot", overview = "First episode",
            id = 1, productionCode = "101", runtime = 45, seasonNumber = 1,
            stillPath = "/still.jpg", voteAverage = 8.5f, voteCount = 500
        )

        val dto = TvSeasonDetailsDto(
            _id = "season_1",
            airDate = "2023-01-01",
            episodes = listOf(episodeDto),
            name = "Season 1",
            overview = "First season",
            id = 1,
            posterPath = "/season1.jpg",
            seasonNumber = 1,
            voteAverage = 9.0f
        )

        val result = dto.toDomain()

        assertEquals("season_1", result._id)
        assertEquals("2023-01-01", result.airDate)
        assertEquals(1, result.episodes.size)
        assertEquals("Pilot", result.episodes[0].name)
        assertEquals("Season 1", result.name)
        assertEquals("First season", result.overview)
        assertEquals(1, result.id)
        assertEquals("/season1.jpg", result.posterPath)
        assertEquals(1, result.seasonNumber)
        assertEquals(9.0f, result.voteAverage)
    }

    @Test
    fun `TvEpisodeDetailsDto toDomain should map correctly`() {
        val crewDto = CrewMemberDto(
            adult = false, gender = 1, id = 1, knownForDepartment = "Writing",
            name = "Writer", originalName = "Writer", popularity = 10f,
            profilePath = "/writer.jpg", creditId = "c1",
            department = "Writing", job = "Writer"
        )

        val dto = TvEpisodeDetailsDto(
            airDate = "2023-01-15",
            crew = listOf(crewDto),
            episodeNumber = 5,
            guestStars = emptyList(),
            name = "Episode 5",
            overview = "Episode overview",
            id = 5,
            productionCode = "E105",
            runtime = 42,
            seasonNumber = 1,
            stillPath = "/ep5.jpg",
            voteAverage = 8.8f,
            voteCount = 250
        )

        val result = dto.toDomain()

        assertEquals("2023-01-15", result.airDate)
        assertEquals(1, result.crew.size)
        assertEquals("Writer", result.crew[0].name)
        assertEquals(5, result.episodeNumber)
        assertEquals("Episode 5", result.name)
        assertEquals("Episode overview", result.overview)
        assertEquals(5, result.id)
        assertEquals("E105", result.productionCode)
        assertEquals(42, result.runtime)
        assertEquals(1, result.seasonNumber)
        assertEquals("/ep5.jpg", result.stillPath)
        assertEquals(8.8f, result.voteAverage)
        assertEquals(250, result.voteCount)
    }

    @Test
    fun `CreatorDto toDomain should map correctly`() {
        val dto = CreatorDto(
            id = 123,
            creditId = "credit123",
            name = "Show Creator",
            gender = 2,
            profilePath = "/creator.jpg"
        )

        val result = dto.toDomain()

        assertEquals(123, result.id)
        assertEquals("credit123", result.creditId)
        assertEquals("Show Creator", result.name)
        assertEquals(2, result.gender)
        assertEquals("/creator.jpg", result.profilePath)
    }

    @Test
    fun `NetworkDto toDomain should map correctly`() {
        val dto = NetworkDto(
            name = "Netflix",
            id = 213,
            logoPath = "/netflix.jpg",
            originCountry = "US"
        )

        val result = dto.toDomain()

        assertEquals("Netflix", result.name)
        assertEquals(213, result.id)
        assertEquals("/netflix.jpg", result.logoPath)
        assertEquals("US", result.originCountry)
    }

    @Test
    fun `ReleaseDatesResultDto toDomain should map correctly`() {
        val releaseDateDto = ReleaseDatesDto(
            certification = "R",
            descriptors = listOf("Violence"),
            languageCode = "en",
            note = "Theatrical",
            releaseDate = "2023-05-01",
            type = 3
        )

        val dto = ReleaseDatesResultDto(
            countryCode = "US",
            releaseDates = listOf(releaseDateDto)
        )

        val result = dto.toDomain()

        assertEquals("US", result.countryCode)
        assertEquals(1, result.releaseDates.size)
        assertEquals("R", result.releaseDates[0].certification)
        assertEquals("Theatrical", result.releaseDates[0].note)
    }

    @Test
    fun `MovieCollectionDto toDomain should map correctly`() {
        val dto = MovieCollectionDto(
            id = 500,
            name = "Marvel Cinematic Universe",
            posterPath = "/mcu_poster.jpg",
            backdropPath = "/mcu_backdrop.jpg"
        )

        val result = dto.toDomain()

        assertEquals(500, result.id)
        assertEquals("Marvel Cinematic Universe", result.name)
        assertEquals("/mcu_poster.jpg", result.posterPath)
        assertEquals("/mcu_backdrop.jpg", result.backdropPath)
    }

    @Test
    fun `MovieCollectionDto toDomain should handle nulls`() {
        val dto = MovieCollectionDto(
            id = null,
            name = null,
            posterPath = null,
            backdropPath = null
        )

        val result = dto.toDomain()

        assertEquals(INVALID_ID, result.id)
        assertEquals("", result.name)
        assertEquals("", result.posterPath)
        assertEquals("", result.backdropPath)
    }

    @Test
    fun `ProductionCountryDto toDomain should map correctly`() {
        val dto = ProductionCountryDto(
            countryCode = "FR",
            name = "France"
        )

        val result = dto.toDomain()

        assertEquals("FR", result.countryCode)
        assertEquals("France", result.name)
    }

    @Test
    fun `MovieDetailsDto with empty collections should map to empty lists`() {
        val dto = MovieDetailsDto(
            adult = false,
            backdropPath = "/backdrop.jpg",
            belongsToCollection = null,
            budget = 0,
            genres = emptyList(),
            homePage = "",
            id = 1,
            imdbId = "",
            originalLanguage = "en",
            originalTitle = "Title",
            overview = "Overview",
            popularity = 10f,
            posterPath = "/poster.jpg",
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            releaseDate = "2023-01-01",
            revenue = 0,
            runtime = 90,
            spokenLanguages = emptyList(),
            status = "Released",
            tagline = "",
            title = "Title",
            video = false,
            voteAverage = 7f,
            voteCount = 100
        )

        val result = dto.toDomain()

        assertTrue(result.genres.isEmpty())
        assertTrue(result.productionCompanies.isEmpty())
        assertTrue(result.productionCountries.isEmpty())
        assertTrue(result.spokenLanguages.isEmpty())
    }

    @Test
    fun `TvSeriesDetailsDto with nested null episode should use empty default`() {
        val dto = TvSeriesDetailsDto(
            adult = false,
            backdropPath = "/backdrop.jpg",
            createdBy = emptyList(),
            episodeRunTime = emptyList(),
            firstAirDate = "2023-01-01",
            genres = emptyList(),
            homepage = "",
            id = 1,
            inProduction = false,
            languages = emptyList(),
            lastAirDate = "2023-12-31",
            lastEpisodeToAir = null,
            name = "Series",
            nextEpisodeToAir = null,
            networks = emptyList(),
            numberOfEpisodes = 0,
            numberOfSeasons = 0,
            originCountry = emptyList(),
            originalLanguage = "en",
            originalName = "Series",
            overview = "Overview",
            popularity = 10f,
            posterPath = "/poster.jpg",
            productionCompanies = emptyList(),
            seasons = emptyList(),
            spokenLanguages = emptyList(),
            status = "Ended",
            tagline = "",
            type = "Scripted",
            voteAverage = 7f,
            voteCount = 100,
            productionCountries = emptyList()
        )

        val result = dto.toDomain()

        assertEquals(INVALID_ID, result.lastEpisodeToAir.id)
        assertNotNull(result.nextEpisodeToAir)
        assertEquals(INVALID_ID, result.nextEpisodeToAir?.id)
        assertEquals("", result.nextEpisodeToAir?.name)
    }
}

