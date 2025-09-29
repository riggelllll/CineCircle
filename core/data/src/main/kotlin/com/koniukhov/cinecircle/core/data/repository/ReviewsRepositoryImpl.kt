package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteRemoteReviewsDataSourceImpl
import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.domain.repository.ReviewsRepository
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(private val remoteReviewsDataSourceImpl: RemoteRemoteReviewsDataSourceImpl) : ReviewsRepository {
    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int,
        language: String
    ): List<MediaReview> {
        val tdo = remoteReviewsDataSourceImpl.getMovieReviews(movieId, page, language)
        return tdo.results.map { it.toDomain() }
    }

    override suspend fun getTvSeriesReviews(
        tvSeriesId: Int,
        page: Int,
        language: String
    ): List<MediaReview> {
        val tdo = remoteReviewsDataSourceImpl.getTvSeriesReviews(tvSeriesId, page, language)
        return tdo.results.map { it.toDomain() }
    }
}