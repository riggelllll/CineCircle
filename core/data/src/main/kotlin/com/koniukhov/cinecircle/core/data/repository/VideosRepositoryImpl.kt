package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteVideosDataSource
import com.koniukhov.cinecircle.core.domain.model.MovieVideos
import com.koniukhov.cinecircle.core.domain.repository.VideosRepository
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(private val remoteVideosDataSource: RemoteVideosDataSource) : VideosRepository{
    override suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MovieVideos {
        val dto = remoteVideosDataSource.getMovieVideos(movieId, language)
        return dto.toDomain()
    }

    override suspend fun getTvSeriesVideos(
        tvSeriesId: Int,
        language: String
    ): MovieVideos {
        val dto = remoteVideosDataSource.getTvSeriesVideos(tvSeriesId, language)
        return dto.toDomain()
    }
}