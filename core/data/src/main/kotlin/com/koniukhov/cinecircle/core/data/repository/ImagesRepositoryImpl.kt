package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteImagesDataSourceImpl
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.repository.ImagesRepository
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(private val remoteImagesDataSourceImpl: RemoteImagesDataSourceImpl): ImagesRepository{
    override suspend fun getMovieImages(
        movieId: Int,
        language: String
    ): MediaImages {
        val dto = remoteImagesDataSourceImpl.getMovieImages(movieId, language)
        return dto.toDomain()
    }

    override suspend fun getTvSeriesImages(
        tvSeriesId: Int,
        language: String
    ): MediaImages {
        val dto = remoteImagesDataSourceImpl.getTvSeriesImages(tvSeriesId, language)
        return dto.toDomain()
    }
}