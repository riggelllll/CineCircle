package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.network.model.MediaCreditsDto

interface RemoteCreditsDatasource {
    suspend fun getMovieCredits(movieId: Int, language: String): MediaCreditsDto
    suspend fun getTvSeriesCredits(tvSeriesId: Int, language: String): MediaCreditsDto
}