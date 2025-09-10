package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.MovieReview

interface ReviewsRepository {
    suspend fun getMovieReviews(movieId: Int, page: Int, language: String): List<MovieReview>
    suspend fun getTvSeriesReviews(tvSeriesId: Int, page: Int, language: String): List<MovieReview>
}