package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.MediaCredits

interface CreditsRepository {
    suspend fun getMovieCredits(movieId: Int, language: String): MediaCredits
    suspend fun getTvSeriesCredits(tvSeriesId: Int, language: String): MediaCredits
}