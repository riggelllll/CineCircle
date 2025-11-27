package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteVideosDataSourceImpl
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.MediaVideos
import com.koniukhov.cinecircle.core.domain.repository.VideosRepository
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(
    private val remoteVideosDataSourceImpl: RemoteVideosDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
) : VideosRepository{
    override suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MediaVideos {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteVideosDataSourceImpl.getMovieVideos(movieId, language)
                dto.toDomain() },
            localCall = { MediaVideos.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MediaVideos.empty()
    }

    override suspend fun getTvSeriesVideos(
        tvSeriesId: Int,
        language: String
    ): MediaVideos {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteVideosDataSourceImpl.getTvSeriesVideos(tvSeriesId, language)
                dto.toDomain() },
            localCall = { MediaVideos.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MediaVideos.empty()
    }
}