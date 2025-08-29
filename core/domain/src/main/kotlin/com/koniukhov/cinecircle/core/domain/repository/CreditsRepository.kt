package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.MovieCredits

interface CreditsRepository {
    suspend fun getMovieCredits(movieId: Int, language: String): MovieCredits
}