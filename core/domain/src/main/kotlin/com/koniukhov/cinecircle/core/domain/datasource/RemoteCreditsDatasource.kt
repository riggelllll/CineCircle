package com.koniukhov.cinecircle.core.domain.datasource

import com.koniukhov.cinecircle.core.network.model.MediaCreditsDto

interface RemoteCreditsDatasource {
    suspend fun getMovieCredits(movieId: Int, language: String): MediaCreditsDto
    suspend fun getTvSeriesCredits(tvSeriesId: Int, language: String): MediaCreditsDto
}