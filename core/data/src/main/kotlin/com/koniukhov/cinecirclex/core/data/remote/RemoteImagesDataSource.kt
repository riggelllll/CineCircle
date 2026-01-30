package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.network.model.MediaImagesDto

interface RemoteImagesDataSource {
    suspend fun getMovieImages(movieId: Int, language: String): MediaImagesDto
    suspend fun getTvSeriesImages(tvSeriesId: Int, language: String): MediaImagesDto
}