package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.network.model.MovieReviewsResponseDto

interface RemoteReviewsDataSource {
    suspend fun getMovieReviews(movieId: Int, page: Int, language: String): MovieReviewsResponseDto
    suspend fun getTvSeriesReviews(tvSeriesId: Int, page: Int, language: String): MovieReviewsResponseDto
}