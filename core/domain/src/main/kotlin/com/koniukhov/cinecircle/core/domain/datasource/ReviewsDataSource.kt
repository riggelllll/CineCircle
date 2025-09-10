package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MovieReviewsResponseDto

interface ReviewsDataSource {
    suspend fun getMovieReviews(movieId: Int, page: Int, language: String): MovieReviewsResponseDto
    suspend fun getTvSeriesReviews(tvSeriesId: Int, page: Int, language: String): MovieReviewsResponseDto
}