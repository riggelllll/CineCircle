package com.koniukhov.cinecirclex.core.data.repository

import com.koniukhov.cinecirclex.core.data.mapper.toDomain
import com.koniukhov.cinecirclex.core.data.remote.RemoteImagesDataSourceImpl
import com.koniukhov.cinecirclex.core.data.util.fetchWithLocalAndRetry
import com.koniukhov.cinecirclex.core.domain.NetworkStatusProvider
import com.koniukhov.cinecirclex.core.domain.model.MediaImages
import com.koniukhov.cinecirclex.core.domain.repository.ImagesRepository
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(
    private val remoteImagesDataSourceImpl: RemoteImagesDataSourceImpl,
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