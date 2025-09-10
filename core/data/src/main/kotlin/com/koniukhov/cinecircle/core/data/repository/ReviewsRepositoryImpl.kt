package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteReviewsDataSource
import com.koniukhov.cinecircle.core.domain.model.MovieReview
import com.koniukhov.cinecircle.core.domain.repository.ReviewsRepository
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(private val remoteReviewsDataSource: RemoteReviewsDataSource) : ReviewsRepository {
    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int,
        language: String
    ): List<MovieReview> {
        val tdo = remoteReviewsDataSource.getMovieReviews(movieId, page, language)
        return tdo.results.map { it.toDomain() }
    }

    override suspend fun getTvSeriesReviews(
        tvSeriesId: Int,
        page: Int,
        language: String
    ): List<MovieReview> {
        val tdo = remoteReviewsDataSource.getTvSeriesReviews(tvSeriesId, page, language)
        return tdo.results.map { it.toDomain() }
    }
}