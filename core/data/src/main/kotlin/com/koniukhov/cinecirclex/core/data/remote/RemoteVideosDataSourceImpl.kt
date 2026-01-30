package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.data.BuildConfig
import com.koniukhov.cinecirclex.core.network.api.TMDBApi
import com.koniukhov.cinecirclex.core.network.model.MediaVideosDto
import javax.inject.Inject

class RemoteVideosDataSourceImpl @Inject constructor(private val api: TMDBApi) : RemoteVideosDataSource {
    override suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MediaVideosDto {
        return api.getMovieVideos(movieId, BuildConfig.API_KEY, language).body() ?: MediaVideosDto.empty()
    }

    override suspend fun getTvSeriesVideos(
        tvSeriesId: Int,
        language: String
    ): MediaVideosDto {
        return api.getTvSeriesVideos(tvSeriesId, BuildConfig.API_KEY, language).body() ?: MediaVideosDto.empty()
    }
}