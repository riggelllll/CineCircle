package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteVideosDataSourceImpl
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.MovieVideos
import com.koniukhov.cinecircle.core.domain.repository.VideosRepository
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(
    private val remoteVideosDataSourceImpl: RemoteRemoteVideosDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : VideosRepository{
    override suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MovieVideos {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteVideosDataSourceImpl.getMovieVideos(movieId, language)
                dto.toDomain() },
            localCall = { MovieVideos.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MovieVideos.empty()
    }

    override suspend fun getTvSeriesVideos(
        tvSeriesId: Int,
        language: String
    ): MovieVideos {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteVideosDataSourceImpl.getTvSeriesVideos(tvSeriesId, language)
                dto.toDomain() },
            localCall = { MovieVideos.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MovieVideos.empty()
    }
}