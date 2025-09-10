package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.MediaImages

interface ImagesRepository {
    suspend fun getMovieImages(movieId: Int, language: String): MediaImages
    suspend fun getTvSeriesImages(tvSeriesId: Int, language: String): MediaImages
}