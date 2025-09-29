package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.VideosDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.MovieVideosDto
import javax.inject.Inject

class RemoteVideosDataSourceImpl @Inject constructor(private val api: TMDBApi) : VideosDataSource {
    override suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MovieVideosDto {
        return api.getMovieVideos(movieId, BuildConfig.API_KEY, language).body() ?: MovieVideosDto.empty()
    }

    override suspend fun getTvSeriesVideos(
        tvSeriesId: Int,
        language: String
    ): MovieVideosDto {
        return api.getTvSeriesVideos(tvSeriesId, BuildConfig.API_KEY, language).body() ?: MovieVideosDto.empty()
    }
}