package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteImagesDataSourceImpl
import com.koniukhov.cinecircle.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.repository.ImagesRepository
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(
    private val remoteImagesDataSourceImpl: RemoteRemoteImagesDataSourceImpl,
    private val networkStatusProvider: NetworkStatusProvider
): ImagesRepository{
    override suspend fun getMovieImages(
        movieId: Int,
        language: String
    ): MediaImages {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteImagesDataSourceImpl.getMovieImages(movieId, language)
                dto.toDomain() },
            localCall = { MediaImages.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MediaImages.empty()
    }

    override suspend fun getTvSeriesImages(
        tvSeriesId: Int,
        language: String
    ): MediaImages {
        return fetchWithLocalAndRetry(
            remoteCall = {
                val dto = remoteImagesDataSourceImpl.getTvSeriesImages(tvSeriesId, language)
                dto.toDomain() },
            localCall = { MediaImages.empty() },
            isNetworkAvailable = { networkStatusProvider.isNetworkAvailable() }
        ) ?: MediaImages.empty()
    }
}