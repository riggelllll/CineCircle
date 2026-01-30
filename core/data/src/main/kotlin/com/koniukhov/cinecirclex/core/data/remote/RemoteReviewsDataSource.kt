package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.network.model.MovieReviewsResponseDto

interface RemoteReviewsDataSource {
    suspend fun getMovieReviews(movieId: Int, page: Int, language: String): MovieReviewsResponseDto
    suspend fun getTvSeriesReviews(tvSeriesId: Int, page: Int, language: String): MovieReviewsResponseDto
}