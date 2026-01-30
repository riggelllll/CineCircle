package com.koniukhov.cinecirclex.core.domain.repository

import com.koniukhov.cinecirclex.core.domain.model.ReleaseDateResult

interface ReleaseDatesRepository {
    suspend fun getMovieReleaseDates(movieId: Int): List<ReleaseDateResult>
}