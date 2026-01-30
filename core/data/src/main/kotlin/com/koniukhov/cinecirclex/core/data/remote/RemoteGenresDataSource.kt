package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.network.model.GenreDto

interface RemoteGenresDataSource {
    suspend fun getMoviesGenreList(language: String): List<GenreDto>
    suspend fun getTvSeriesGenreList(language: String): List<GenreDto>
}