package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.RemoteImagesDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.MediaImagesDto
import javax.inject.Inject

class RemoteRemoteImagesDataSourceImpl @Inject constructor(private val api: TMDBApi): RemoteImagesDataSource {
    override suspend fun getMovieImages(
        movieId: Int,
        language: String
    ): MediaImagesDto {
        return api.getMovieImages(movieId, BuildConfig.API_KEY, language).body() ?: MediaImagesDto.empty()
    }

    override suspend fun getTvSeriesImages(
        tvSeriesId: Int,
        language: String
    ): MediaImagesDto {
        return api.getTvSeriesImages(tvSeriesId, BuildConfig.API_KEY, language).body() ?: MediaImagesDto.empty()
    }
}