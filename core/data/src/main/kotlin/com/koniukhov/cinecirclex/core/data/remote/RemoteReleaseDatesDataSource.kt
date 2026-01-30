package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.network.model.ReleaseDatesResponseDto

interface RemoteReleaseDatesDataSource {
    suspend fun getMovieReleaseDates(movieId: Int): ReleaseDatesResponseDto
}