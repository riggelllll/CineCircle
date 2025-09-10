package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.ReviewsDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.MovieReviewsResponseDto
import javax.inject.Inject

class RemoteReviewsDataSource @Inject constructor(private val api: TMDBApi) : ReviewsDataSource {
    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int,
        language: String
    ): MovieReviewsResponseDto {
        return api.getMovieReviews(movieId, BuildConfig.API_KEY, page, language).body() ?: MovieReviewsResponseDto.empty()
    }

    override suspend fun getTvSeriesReviews(
        tvSeriesId: Int,
        page: Int,
        language: String
    ): MovieReviewsResponseDto {
        return api.getTvSeriesReviews(tvSeriesId, BuildConfig.API_KEY, page, language).body() ?: MovieReviewsResponseDto.empty()
    }
}