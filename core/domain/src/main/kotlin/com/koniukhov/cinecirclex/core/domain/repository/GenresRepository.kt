package com.koniukhov.cinecirclex.core.domain.repository

import com.koniukhov.cinecirclex.core.domain.model.Genre

interface GenresRepository {
    suspend fun getMoviesGenreList(language: String): List<Genre>
    suspend fun getTvSeriesGenreList(language: String): List<Genre>
}