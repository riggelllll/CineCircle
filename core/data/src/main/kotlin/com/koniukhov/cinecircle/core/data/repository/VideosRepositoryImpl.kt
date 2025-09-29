package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteVideosDataSourceImpl
import com.koniukhov.cinecircle.core.domain.model.MovieVideos
import com.koniukhov.cinecircle.core.domain.repository.VideosRepository
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(private val remoteVideosDataSourceImpl: RemoteRemoteVideosDataSourceImpl) : VideosRepository{
    override suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MovieVideos {
        val dto = remoteVideosDataSourceImpl.getMovieVideos(movieId, language)
        return dto.toDomain()
    }

    override suspend fun getTvSeriesVideos(
        tvSeriesId: Int,
        language: String
    ): MovieVideos {
        val dto = remoteVideosDataSourceImpl.getTvSeriesVideos(tvSeriesId, language)
        return dto.toDomain()
    }
}