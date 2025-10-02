package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.network.model.MediaVideosDto

interface RemoteVideosDataSource {
    suspend fun getMovieVideos(movieId: Int, language: String): MediaVideosDto
    suspend fun getTvSeriesVideos(tvSeriesId: Int, language: String): MediaVideosDto
}