package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.MovieVideos

interface VideosRepository {
    suspend fun getMovieVideos(movieId: Int, language: String): MovieVideos
    suspend fun getTvSeriesVideos(tvSeriesId: Int, language: String): MovieVideos
}