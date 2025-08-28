package com.koniukhov.cinecircle.core.data.mapper

import com.koniukhov.cinecircle.core.domain.model.CollectionDetails
import com.koniukhov.cinecircle.core.domain.model.CollectionMedia
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.model.Company
import com.koniukhov.cinecircle.core.domain.model.Image
import com.koniukhov.cinecircle.core.domain.model.ProductionCountry
import com.koniukhov.cinecircle.core.domain.model.Language
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.model.MovieCollection
import com.koniukhov.cinecircle.core.network.model.CollectionDetailsDto
import com.koniukhov.cinecircle.core.network.model.CollectionMediaDto
import com.koniukhov.cinecircle.core.network.model.GenreDto
import com.koniukhov.cinecircle.core.network.model.MovieDetailsDto
import com.koniukhov.cinecircle.core.network.model.MovieDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesDto
import com.koniukhov.cinecircle.core.network.model.CompanyDto
import com.koniukhov.cinecircle.core.network.model.ImageDto
import com.koniukhov.cinecircle.core.network.model.ProductionCountryDto
import com.koniukhov.cinecircle.core.network.model.LanguageDto
import com.koniukhov.cinecircle.core.network.model.MediaImagesDto
import com.koniukhov.cinecircle.core.network.model.MovieCollectionDto

fun MovieDto.toDomain(): Movie = Movie(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    genreIds = genreIds ?: emptyList(),
    id = id ?: 0,
    originalLanguage = originalLanguage ?: "",
    originalTitle = originalTitle ?: "",
    overview = overview ?: "",
    popularity = popularity ?: 0.0f,
    posterPath = posterPath ?: "",
    releaseDate = releaseDate ?: "",
    title = title ?: "",
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0f,
    voteCount = voteCount ?: 0
)

fun TvSeriesDto.toDomain(): TvSeries = TvSeries(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    genreIds = genreIds ?: emptyList(),
    id = id ?: 0,
    originCountry = originCountry ?: emptyList(),
    originalLanguage = originalLanguage ?: "",
    originalName = originalName ?: "",
    overview = overview ?: "",
    popularity = popularity ?: 0.0f,
    posterPath = posterPath ?: "",
    firstAirDate = firstAirDate ?: "",
    name = name ?: "",
    voteAverage = voteAverage ?: 0.0f,
    voteCount = voteCount ?: 0
)

fun GenreDto.toDomain(): Genre =
    Genre(
        id = id ?: 0,
        name = name ?: ""
)

fun MovieDetailsDto.toDomain(): MovieDetails = MovieDetails(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    belongsToCollection = belongsToCollection?.toDomain() ?: MovieCollection(-1, "", "", ""),
    budget = budget ?: 0,
    genres = genres?.map { it.toDomain() } ?: emptyList(),
    homePage = homePage ?: "",
    id = id ?: 0,
    imdbId = imdbId ?: "",
    originalLanguage = originalLanguage ?: "",
    originalTitle = originalTitle ?: "",
    overview = overview ?: "",
    popularity = popularity ?: 0.0f,
    posterPath = posterPath ?: "",
    productionCompanies = productionCompanies?.map { it.toDomain() } ?: emptyList(),
    productionCountries = productionCountries?.map { it.toDomain() } ?: emptyList(),
    releaseDate = releaseDate ?: "",
    revenue = revenue ?: 0,
    runtime = runtime ?: 0,
    spokenLanguages = spokenLanguages?.map { it.toDomain() } ?: emptyList(),
    status = status ?: "",
    tagline = tagline ?: "",
    title = title ?: "",
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0f,
    voteCount = voteCount ?: 0
)

fun CompanyDto.toDomain(): Company = Company(
    id = id ?: 0,
    logoPath = logoPath ?: "",
    name = name ?: "",
    originCountry = originCountry ?: ""
)

fun ProductionCountryDto.toDomain(): ProductionCountry = ProductionCountry(
    isoName = isoName ?: "",
    name = name ?: ""
)

fun LanguageDto.toDomain(): Language = Language(
    englishName = englishName ?: "",
    isoName = isoName ?: "",
    name = name ?: ""
)

fun MovieCollectionDto.toDomain(): MovieCollection =
    MovieCollection(
        id = id,
        name = name,
        posterPath = posterPath,
        backdropPath = backdropPath
)

fun CollectionMediaDto.toDomain(): CollectionMedia = CollectionMedia(
    adult = adult ?: false,
    backdropPath = backdropPath ?: "",
    id = id ?: 0,
    title = title ?: "",
    originalLanguage = originalLanguage ?: "",
    originalTitle = originalTitle ?: "",
    overview = overview ?: "",
    posterPath = posterPath ?: "",
    mediaType = mediaType ?: "",
    genreIds = genreIds ?: emptyList(),
    popularity = popularity?.toDouble() ?: 0.0,
    releaseDate = releaseDate ?: "",
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0
)

fun CollectionDetailsDto.toDomain(): CollectionDetails = CollectionDetails(
    id = id ?: 0,
    name = name ?: "",
    overview = overview ?: "",
    posterPath = posterPath ?: "",
    backdropPath = backdropPath ?: "",
    parts = parts?.map { it.toDomain() } ?: emptyList()
)

fun ImageDto.toDomain(): Image = Image(
    aspectRatio = aspectRatio ?: 0.0,
    filePath = filePath ?: "",
    height = height ?: 0,
    isoName = isoName ?: "",
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0,
    width = width ?: 0
)

fun MediaImagesDto.toDomain(): MediaImages = MediaImages(
    id = id ?: 0,
    backdrops = backdrops?.map { it.toDomain() } ?: emptyList(),
    posters = posters?.map { it.toDomain() } ?: emptyList(),
    logos = logos?.map { it.toDomain() } ?: emptyList()
)