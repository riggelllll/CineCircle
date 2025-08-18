package com.koniukhov.cinecircle.core.data.mapper

import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.network.model.MovieDto
import com.koniukhov.cinecircle.core.network.model.TvSeriesDto

fun MovieDto.toDomain(): Movie = Movie(
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genreIds,
    id = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun TvSeriesDto.toDomain(): TvSeries = TvSeries(
    adult = adult,
    backdropPath = backdropPath ?: "",
    genreIds = genreIds,
    id = id,
    originCountry = originCountry,
    originalLanguage = originalLanguage,
    originalName = originalName,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    firstAirDate = firstAirDate,
    name = name,
    voteAverage = voteAverage,
    voteCount = voteCount
)