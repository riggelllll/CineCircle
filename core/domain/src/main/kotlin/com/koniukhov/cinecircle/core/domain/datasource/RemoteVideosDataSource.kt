package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MovieVideosDto

interface RemoteVideosDataSource {
    suspend fun getMovieVideos(movieId: Int, language: String): MovieVideosDto
    suspend fun getTvSeriesVideos(tvSeriesId: Int, language: String): MovieVideosDto
}