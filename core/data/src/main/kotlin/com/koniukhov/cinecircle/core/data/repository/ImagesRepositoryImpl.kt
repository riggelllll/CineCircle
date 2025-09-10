package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteImagesDataSource
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.repository.ImagesRepository
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(private val remoteImagesDataSource: RemoteImagesDataSource): ImagesRepository{
    override suspend fun getMovieImages(
        movieId: Int,
        language: String
    ): MediaImages {
        val dto = remoteImagesDataSource.getMovieImages(movieId, language)
        return dto.toDomain()
    }

    override suspend fun getTvSeriesImages(
        tvSeriesId: Int,
        language: String
    ): MediaImages {
        val dto = remoteImagesDataSource.getTvSeriesImages(tvSeriesId, language)
        return dto.toDomain()
    }
}