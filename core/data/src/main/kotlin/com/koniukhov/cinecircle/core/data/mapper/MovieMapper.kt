package com.koniukhov.cinecircle.core.data.mapper

import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.network.model.GenreDto
import com.koniukhov.cinecircle.core.network.model.MovieDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesDto

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