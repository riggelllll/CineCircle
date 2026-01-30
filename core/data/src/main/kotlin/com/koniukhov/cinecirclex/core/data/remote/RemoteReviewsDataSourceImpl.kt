package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.data.BuildConfig
import com.koniukhov.cinecirclex.core.network.api.TMDBApi
import com.koniukhov.cinecirclex.core.network.model.MovieReviewsResponseDto
import javax.inject.Inject

class RemoteReviewsDataSourceImpl @Inject constructor(private val api: TMDBApi) : RemoteReviewsDataSource {
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