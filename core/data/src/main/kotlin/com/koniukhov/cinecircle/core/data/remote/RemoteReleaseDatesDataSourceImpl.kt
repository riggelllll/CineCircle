package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.ReleaseDatesResponseDto
import javax.inject.Inject

class RemoteReleaseDatesDataSourceImpl @Inject constructor(private val api: TMDBApi) : RemoteReleaseDatesDataSource {
    override suspend fun getMovieReleaseDates(movieId: Int): ReleaseDatesResponseDto {
        return api.getMovieReleaseDates(movieId, BuildConfig.API_KEY).body() ?: ReleaseDatesResponseDto.empty()
    }
}