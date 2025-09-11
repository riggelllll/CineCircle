package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MovieCreditsDto

interface CreditsDatasource {
    suspend fun getMovieCredits(movieId: Int, language: String): MovieCreditsDto
    suspend fun getTvSeriesCredits(tvSeriesId: Int, language: String): MovieCreditsDto
}