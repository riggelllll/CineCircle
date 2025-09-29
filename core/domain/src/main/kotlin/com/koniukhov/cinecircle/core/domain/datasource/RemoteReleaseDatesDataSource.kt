package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.ReleaseDatesResponseDto

interface RemoteReleaseDatesDataSource {
    suspend fun getMovieReleaseDates(movieId: Int): ReleaseDatesResponseDto
}