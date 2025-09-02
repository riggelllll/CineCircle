package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.ReleaseDatesResponseDto

interface ReleaseDatesDataSource {
    suspend fun getMovieReleaseDates(movieId: Int): ReleaseDatesResponseDto
}