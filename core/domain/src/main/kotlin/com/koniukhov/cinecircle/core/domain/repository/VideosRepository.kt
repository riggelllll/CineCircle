package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.MediaVideos

interface VideosRepository {
    suspend fun getMovieVideos(movieId: Int, language: String): MediaVideos
    suspend fun getTvSeriesVideos(tvSeriesId: Int, language: String): MediaVideos
}