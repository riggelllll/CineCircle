package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.data.BuildConfig
import com.koniukhov.cinecirclex.core.network.api.TMDBApi
import com.koniukhov.cinecirclex.core.network.model.ReleaseDatesResponseDto
import javax.inject.Inject

class RemoteReleaseDatesDataSourceImpl @Inject constructor(private val api: TMDBApi) : RemoteReleaseDatesDataSource {
    override suspend fun getMovieReleaseDates(movieId: Int): ReleaseDatesResponseDto {
        return api.getMovieReleaseDates(movieId, BuildConfig.API_KEY).body() ?: ReleaseDatesResponseDto.empty()
    }
}