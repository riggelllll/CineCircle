package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.ReleaseDateResult

interface ReleaseDatesRepository {
    suspend fun getMovieReleaseDates(movieId: Int): List<ReleaseDateResult>
}