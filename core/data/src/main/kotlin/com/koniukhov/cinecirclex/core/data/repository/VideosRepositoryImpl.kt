package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.mapper.toDomain
import com.koniukhov.cinecirclex.core.data.remote.RemoteVideosDataSourceImpl
import com.koniukhov.cinecirclex.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.domain.model.MediaVideos
import com.koniukhov.cinecirclex.core.domain.repository.VideosRepository
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