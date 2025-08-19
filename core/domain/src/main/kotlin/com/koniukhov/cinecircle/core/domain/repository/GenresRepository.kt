package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.Genre

interface GenresRepository {
    suspend fun getMoviesGenreList(language: String): List<Genre>
    suspend fun getTvSeriesGenreList(language: String): List<Genre>
}