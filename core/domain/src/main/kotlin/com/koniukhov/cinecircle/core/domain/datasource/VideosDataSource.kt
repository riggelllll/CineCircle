package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MovieVideosDto

interface VideosDataSource {
    suspend fun getMovieVideos(movieId: Int, language: String): MovieVideosDto
}