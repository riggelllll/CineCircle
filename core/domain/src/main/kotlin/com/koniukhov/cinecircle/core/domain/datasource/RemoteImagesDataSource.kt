package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MediaImagesDto

interface RemoteImagesDataSource {
    suspend fun getMovieImages(movieId: Int, language: String): MediaImagesDto
    suspend fun getTvSeriesImages(tvSeriesId: Int, language: String): MediaImagesDto
}