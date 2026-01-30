package com.koniukhov.cinecirclex.core.data.mapper

import com.koniukhov.cinecirclex.core.common.Constants.INVALID_ID
import com.koniukhov.cinecirclex.core.network.model.CastMemberDto
import com.koniukhov.cinecirclex.core.network.model.CollectionDetailsDto
import com.koniukhov.cinecirclex.core.network.model.CollectionMediaDto
import com.koniukhov.cinecirclex.core.network.model.ContentRatingDto
import com.koniukhov.cinecirclex.core.network.model.CrewMemberDto
import com.koniukhov.cinecirclex.core.network.model.GenreDto
import com.koniukhov.cinecirclex.core.network.model.ImageDto
import com.koniukhov.cinecirclex.core.network.model.LanguageDto
import com.koniukhov.cinecirclex.core.network.model.MediaCreditsDto
import com.koniukhov.cinecirclex.core.network.model.MediaImagesDto
import com.koniukhov.cinecirclex.core.network.model.MediaReviewDto
import com.koniukhov.cinecirclex.core.network.model.MediaVideosDto
import com.koniukhov.cinecirclex.core.network.model.MovieDto
import com.koniukhov.cinecirclex.core.network.model.ProductionCompanyDto
import com.koniukhov.cinecirclex.core.network.model.ReleaseDatesDto
import com.koniukhov.cinecirclex.core.network.model.ReviewAuthorDto
import com.koniukhov.cinecirclex.core.network.model.TvSeriesDto
import com.koniukhov.cinecirclex.core.network.model.VideoDto
import org.junit.Assert.*
import org.junit.Test

class MediaMapperTest {

    @Test
    fun `MovieDto toDomain should map all fields correctly`() {
        val movieDto = MovieDto(
            adult = true,
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(28, 12),
            id = 123,
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Movie overview",
            popularity = 100.5f,
            posterPath = "/poster.jpg",
            releaseDate = "2023-01-01",
            title = "Movie Title",
            video = false,
            voteAverage = 8.5f,
            voteCount = 1000
        )

        val result = movieDto.toDomain()

        assertEquals(true, result.adult)
        assertEquals("/backdrop.jpg", result.backdropPath)
        assertEquals(listOf(28, 12), result.genreIds)
        assertEquals(123, result.id)
        assertEquals("en", result.originalLanguage)
        assertEquals("Original Title", result.originalTitle)
        assertEquals("Movie overview", result.overview)
        assertEquals(100.5f, result.popularity)
        assertEquals("/poster.jpg", result.posterPath)
        assertEquals("2023-01-01", result.releaseDate)
        assertEquals("Movie Title", result.title)
        assertEquals(false, result.video)
        assertEquals(8.5f, result.voteAverage)
        assertEquals(1000, result.voteCount)
    }

    @Test
    fun `MovieDto toDomain should handle null values with defaults`() {
        val movieDto = MovieDto(
            adult = null,
            backdropPath = null,
            genreIds = null,
            id = null,
            originalLanguage = null,
            originalTitle = null,
            overview = null,
            popularity = null,
            posterPath = null,
            releaseDate = null,
            title = null,
            video = null,
            voteAverage = null,
            voteCount = null
        )

        val result = movieDto.toDomain()

        assertEquals(false, result.adult)
        assertEquals("", result.backdropPath)
        assertEquals(emptyList<Int>(), result.genreIds)
        assertEquals(INVALID_ID, result.id)
        assertEquals("", result.originalLanguage)
        assertEquals("", result.originalTitle)
        assertEquals("", result.overview)
        assertEquals(0.0f, result.popularity)
        assertEquals("", result.posterPath)
        assertEquals("", result.releaseDate)
        assertEquals("", result.title)
        assertEquals(false, result.video)
        assertEquals(0.0f, result.voteAverage)
        assertEquals(0, result.voteCount)
    }

    @Test
    fun `TvSeriesDto toDomain should map all fields correctly`() {
        val tvSeriesDto = TvSeriesDto(
            adult = false,
            backdropPath = "/tv_backdrop.jpg",
            genreIds = listOf(18, 10765),
            id = 456,
            originCountry = listOf("US", "UK"),
            originalLanguage = "en",
            originalName = "Original Series Name",
            overview = "Series overview",
            popularity = 200.5f,
            posterPath = "/tv_poster.jpg",
            firstAirDate = "2022-05-01",
            name = "Series Name",
            voteAverage = 9.0f,
            voteCount = 2000
        )

        val result = tvSeriesDto.toDomain()

        assertEquals(false, result.adult)
        assertEquals("/tv_backdrop.jpg", result.backdropPath)
        assertEquals(listOf(18, 10765), result.genreIds)
        assertEquals(456, result.id)
        assertEquals(listOf("US", "UK"), result.originCountry)
        assertEquals("en", result.originalLanguage)
        assertEquals("Original Series Name", result.originalName)
        assertEquals("Series overview", result.overview)
        assertEquals(200.5f, result.popularity)
        assertEquals("/tv_poster.jpg", result.posterPath)
        assertEquals("2022-05-01", result.firstAirDate)
        assertEquals("Series Name", result.title)
        assertEquals(9.0f, result.voteAverage)
        assertEquals(2000, result.voteCount)
    }

    @Test
    fun `TvSeriesDto toDomain should handle null values with defaults`() {
        val tvSeriesDto = TvSeriesDto(
            adult = null,
            backdropPath = null,
            genreIds = null,
            id = null,
            originCountry = null,
            originalLanguage = null,
            originalName = null,
            overview = null,
            popularity = null,
            posterPath = null,
            firstAirDate = null,
            name = null,
            voteAverage = null,
            voteCount = null
        )

        val result = tvSeriesDto.toDomain()

        assertEquals(false, result.adult)
        assertEquals("", result.backdropPath)
        assertEquals(emptyList<Int>(), result.genreIds)
        assertEquals(INVALID_ID, result.id)
        assertEquals(emptyList<String>(), result.originCountry)
        assertEquals("", result.originalLanguage)
        assertEquals("", result.originalName)
        assertEquals("", result.overview)
        assertEquals(0.0f, result.popularity)
        assertEquals("", result.posterPath)
        assertEquals("", result.firstAirDate)
        assertEquals("", result.title)
        assertEquals(0.0f, result.voteAverage)
        assertEquals(0, result.voteCount)
    }

    @Test
    fun `GenreDto toDomain should map correctly`() {
        val genreDto = GenreDto(id = 28, name = "Action")

        val result = genreDto.toDomain()

        assertEquals(28, result.id)
        assertEquals("Action", result.name)
    }

    @Test
    fun `GenreDto toDomain should handle null values`() {
        val genreDto = GenreDto(id = null, name = null)

        val result = genreDto.toDomain()

        assertEquals(INVALID_ID, result.id)
        assertEquals("", result.name)
    }

    @Test
    fun `ProductionCompanyDto toDomain should map correctly`() {
        val dto = ProductionCompanyDto(
            id = 1,
            logoPath = "/logo.jpg",
            name = "Warner Bros",
            originCountry = "US"
        )

        val result = dto.toDomain()

        assertEquals(1, result.id)
        assertEquals("/logo.jpg", result.logoPath)
        assertEquals("Warner Bros", result.name)
        assertEquals("US", result.originCountry)
    }

    @Test
    fun `ProductionCompanyDto toDomain should handle nulls`() {
        val dto = ProductionCompanyDto(
            id = null,
            logoPath = null,
            name = null,
            originCountry = null
        )

        val result = dto.toDomain()

        assertEquals(INVALID_ID, result.id)
        assertEquals("", result.logoPath)
        assertEquals("", result.name)
        assertEquals("", result.originCountry)
    }

    @Test
    fun `LanguageDto toDomain should map correctly`() {
        val dto = LanguageDto(
            englishName = "English",
            languageCode = "en",
            name = "English"
        )

        val result = dto.toDomain()

        assertEquals("English", result.englishName)
        assertEquals("en", result.languageCode)
        assertEquals("English", result.name)
    }

    @Test
    fun `LanguageDto toDomain should handle nulls`() {
        val dto = LanguageDto(
            englishName = null,
            languageCode = null,
            name = null
        )

        val result = dto.toDomain()

        assertEquals("", result.englishName)
        assertEquals("", result.languageCode)
        assertEquals("", result.name)
    }

    @Test
    fun `ImageDto toDomain should map correctly`() {
        val dto = ImageDto(
            aspectRatio = 1.78f,
            filePath = "/image.jpg",
            height = 1080,
            countryCode = "US",
            voteAverage = 7.5f,
            voteCount = 100,
            width = 1920
        )

        val result = dto.toDomain()

        assertEquals(1.78f, result.aspectRatio)
        assertEquals("/image.jpg", result.filePath)
        assertEquals(1080, result.height)
        assertEquals("US", result.countryCode)
        assertEquals(7.5f, result.voteAverage)
        assertEquals(100, result.voteCount)
        assertEquals(1920, result.width)
    }

    @Test
    fun `ImageDto toDomain should handle nulls`() {
        val dto = ImageDto(
            aspectRatio = null,
            filePath = null,
            height = null,
            countryCode = null,
            voteAverage = null,
            voteCount = null,
            width = null
        )

        val result = dto.toDomain()

        assertEquals(0.0f, result.aspectRatio)
        assertEquals("", result.filePath)
        assertEquals(0, result.height)
        assertEquals("", result.countryCode)
        assertEquals(0.0f, result.voteAverage)
        assertEquals(0, result.voteCount)
        assertEquals(0, result.width)
    }

    @Test
    fun `VideoDto toDomain should map correctly`() {
        val dto = VideoDto(
            languageCode = "en",
            countryCode = "US",
            name = "Official Trailer",
            key = "abc123",
            site = "YouTube",
            size = 1080,
            type = "Trailer",
            official = true,
            publishedAt = "2023-01-01T00:00:00Z",
            id = "video1"
        )

        val result = dto.toDomain()

        assertEquals("en", result.languageCode)
        assertEquals("US", result.countryCode)
        assertEquals("Official Trailer", result.name)
        assertEquals("abc123", result.key)
        assertEquals("YouTube", result.site)
        assertEquals(1080, result.size)
        assertEquals("Trailer", result.type)
        assertEquals(true, result.official)
        assertEquals("2023-01-01T00:00:00Z", result.publishedAt)
        assertEquals("video1", result.id)
    }

    @Test
    fun `VideoDto toDomain should handle nulls`() {
        val dto = VideoDto(
            languageCode = null,
            countryCode = null,
            name = null,
            key = null,
            site = null,
            size = null,
            type = null,
            official = null,
            publishedAt = null,
            id = null
        )

        val result = dto.toDomain()

        assertEquals("", result.languageCode)
        assertEquals("", result.countryCode)
        assertEquals("", result.name)
        assertEquals("", result.key)
        assertEquals("", result.site)
        assertEquals(0, result.size)
        assertEquals("", result.type)
        assertEquals(false, result.official)
        assertEquals("", result.publishedAt)
        assertEquals("", result.id)
    }

    @Test
    fun `CastMemberDto toDomain should map correctly`() {
        val dto = CastMemberDto(
            adult = false,
            gender = 1,
            id = 123,
            knownForDepartment = "Acting",
            name = "John Doe",
            originalName = "John Doe",
            popularity = 50.5f,
            profilePath = "/profile.jpg",
            castId = 1,
            character = "Hero",
            creditId = "credit123",
            order = 0
        )

        val result = dto.toDomain()

        assertEquals(false, result.adult)
        assertEquals(1, result.gender)
        assertEquals(123, result.id)
        assertEquals("Acting", result.knownForDepartment)
        assertEquals("John Doe", result.name)
        assertEquals("John Doe", result.originalName)
        assertEquals(50.5f, result.popularity)
        assertEquals("/profile.jpg", result.profilePath)
        assertEquals(1, result.castId)
        assertEquals("Hero", result.character)
        assertEquals("credit123", result.creditId)
        assertEquals(0, result.order)
    }

    @Test
    fun `CrewMemberDto toDomain should map correctly`() {
        val dto = CrewMemberDto(
            adult = false,
            gender = 2,
            id = 456,
            knownForDepartment = "Directing",
            name = "Jane Smith",
            originalName = "Jane Smith",
            popularity = 60.0f,
            profilePath = "/director.jpg",
            creditId = "credit456",
            department = "Directing",
            job = "Director"
        )

        val result = dto.toDomain()

        assertEquals(false, result.adult)
        assertEquals(2, result.gender)
        assertEquals(456, result.id)
        assertEquals("Directing", result.knownForDepartment)
        assertEquals("Jane Smith", result.name)
        assertEquals("Jane Smith", result.originalName)
        assertEquals(60.0f, result.popularity)
        assertEquals("/director.jpg", result.profilePath)
        assertEquals("credit456", result.creditId)
        assertEquals("Directing", result.department)
        assertEquals("Director", result.job)
    }

    @Test
    fun `MediaCreditsDto toDomain should map cast and crew lists`() {
        val castDto = CastMemberDto(
            adult = false, gender = 1, id = 1, knownForDepartment = "Acting",
            name = "Actor", originalName = "Actor", popularity = 10f,
            profilePath = "/actor.jpg", castId = 1, character = "Hero",
            creditId = "c1", order = 0
        )
        val crewDto = CrewMemberDto(
            adult = false, gender = 2, id = 2, knownForDepartment = "Directing",
            name = "Director", originalName = "Director", popularity = 20f,
            profilePath = "/director.jpg", creditId = "c2",
            department = "Directing", job = "Director"
        )
        val dto = MediaCreditsDto(
            id = 100,
            cast = listOf(castDto),
            crew = listOf(crewDto)
        )

        val result = dto.toDomain()

        assertEquals(1, result.cast.size)
        assertEquals("Actor", result.cast[0].name)
        assertEquals(1, result.crew.size)
        assertEquals("Director", result.crew[0].name)
    }

    @Test
    fun `MediaCreditsDto toDomain should handle null lists`() {
        val dto = MediaCreditsDto(id = 100, cast = null, crew = null)

        val result = dto.toDomain()

        assertTrue(result.cast.isEmpty())
        assertTrue(result.crew.isEmpty())
    }

    @Test
    fun `ReviewAuthorDto toDomain should map correctly`() {
        val dto = ReviewAuthorDto(
            name = "Reviewer",
            username = "reviewer123",
            avatarPath = "/avatar.jpg",
            rating = "8.0"
        )

        val result = dto.toDomain()

        assertEquals("Reviewer", result.name)
        assertEquals("reviewer123", result.username)
        assertEquals("/avatar.jpg", result.avatarPath)
        assertEquals("8.0", result.rating)
    }

    @Test
    fun `MediaReviewDto toDomain should map correctly`() {
        val authorDto = ReviewAuthorDto("Name", "username", "/avatar.jpg", "9.0")
        val dto = MediaReviewDto(
            author = "Author Name",
            authorDetails = authorDto,
            content = "Great movie!",
            createdAt = "2023-01-01",
            id = "review1",
            updatedAt = "2023-01-02",
            url = "http://review.url"
        )

        val result = dto.toDomain()

        assertEquals("Author Name", result.author)
        assertEquals("Name", result.authorDetails.name)
        assertEquals("Great movie!", result.content)
        assertEquals("2023-01-01", result.createdAt)
        assertEquals("review1", result.id)
        assertEquals("2023-01-02", result.updatedAt)
        assertEquals("http://review.url", result.url)
    }

    @Test
    fun `CollectionMediaDto toDomain should map correctly`() {
        val dto = CollectionMediaDto(
            adult = false,
            backdropPath = "/backdrop.jpg",
            id = 1,
            title = "Movie 1",
            originalLanguage = "en",
            originalTitle = "Movie 1",
            overview = "Overview",
            posterPath = "/poster.jpg",
            mediaType = "movie",
            genreIds = listOf(28),
            popularity = 100f,
            releaseDate = "2023-01-01",
            video = false,
            voteAverage = 8.0f,
            voteCount = 1000
        )

        val result = dto.toDomain()

        assertEquals(false, result.adult)
        assertEquals("/backdrop.jpg", result.backdropPath)
        assertEquals(1, result.id)
        assertEquals("Movie 1", result.title)
        assertEquals("movie", result.mediaType)
    }

    @Test
    fun `CollectionDetailsDto toDomain should map correctly`() {
        val partDto = CollectionMediaDto(
            adult = false, backdropPath = "/b.jpg", id = 1, title = "Part 1",
            originalLanguage = "en", originalTitle = "Part 1", overview = "Ov",
            posterPath = "/p.jpg", mediaType = "movie", genreIds = listOf(28),
            popularity = 100f, releaseDate = "2023-01-01", video = false,
            voteAverage = 8f, voteCount = 100
        )
        val dto = CollectionDetailsDto(
            id = 100,
            name = "Collection",
            overview = "Collection overview",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            parts = listOf(partDto)
        )

        val result = dto.toDomain()

        assertEquals(100, result.id)
        assertEquals("Collection", result.name)
        assertEquals("Collection overview", result.overview)
        assertEquals("/poster.jpg", result.posterPath)
        assertEquals("/backdrop.jpg", result.backdropPath)
        assertEquals(1, result.parts.size)
        assertEquals("Part 1", result.parts[0].title)
    }

    @Test
    fun `ContentRatingDto toDomain should map correctly`() {
        val dto = ContentRatingDto(
            descriptors = listOf("Violence"),
            countryCode = "US",
            rating = "PG-13"
        )

        val result = dto.toDomain()

        assertEquals(listOf("Violence"), result.descriptors)
        assertEquals("US", result.countryCode)
        assertEquals("PG-13", result.rating)
    }

    @Test
    fun `ReleaseDatesDto toDomain should map correctly`() {
        val dto = ReleaseDatesDto(
            certification = "PG-13",
            descriptors = emptyList(),
            languageCode = "en",
            note = "Theatrical",
            releaseDate = "2023-01-01",
            type = 3
        )

        val result = dto.toDomain()

        assertEquals("PG-13", result.certification)
        assertEquals("en", result.languageCode)
        assertEquals("Theatrical", result.note)
        assertEquals("2023-01-01", result.releaseDate)
        assertEquals(3, result.releaseType)
    }

    @Test
    fun `MediaImagesDto toDomain should map all image lists`() {
        val imageDto = ImageDto(
            aspectRatio = 1.78f, filePath = "/img.jpg", height = 1080,
            countryCode = "US", voteAverage = 7f, voteCount = 10, width = 1920
        )
        val dto = MediaImagesDto(
            id = 100,
            backdrops = listOf(imageDto),
            posters = listOf(imageDto),
            logos = listOf(imageDto)
        )

        val result = dto.toDomain()

        assertEquals(100, result.id)
        assertEquals(1, result.backdrops.size)
        assertEquals(1, result.posters.size)
        assertEquals(1, result.logos.size)
    }

    @Test
    fun `MediaVideosDto toDomain should map video list`() {
        val videoDto = VideoDto(
            languageCode = "en", countryCode = "US", name = "Trailer",
            key = "abc", site = "YouTube", size = 1080, type = "Trailer",
            official = true, publishedAt = "2023-01-01", id = "v1"
        )
        val dto = MediaVideosDto(id = 100, results = listOf(videoDto))

        val result = dto.toDomain()

        assertEquals(100, result.id)
        assertEquals(1, result.results.size)
        assertEquals("Trailer", result.results[0].name)
    }
}