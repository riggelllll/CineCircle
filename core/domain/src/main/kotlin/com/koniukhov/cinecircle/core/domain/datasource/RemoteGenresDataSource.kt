package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.GenreDto

interface RemoteGenresDataSource {
    suspend fun getMoviesGenreList(language: String): List<GenreDto>
    suspend fun getTvSeriesGenreList(language: String): List<GenreDto>
}