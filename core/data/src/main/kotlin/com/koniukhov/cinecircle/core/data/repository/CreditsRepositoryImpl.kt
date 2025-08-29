package com.koniukhov.cinecircle.core.data.repository

import com.koniukhov.cinecircle.core.data.mapper.toDomain
import com.koniukhov.cinecircle.core.data.remote.RemoteMovieCreditsDataSource
import com.koniukhov.cinecircle.core.domain.model.MovieCredits
import com.koniukhov.cinecircle.core.domain.repository.CreditsRepository
import javax.inject.Inject

class CreditsRepositoryImpl @Inject constructor(
    private val remoteMovieCreditsDataSource: RemoteMovieCreditsDataSource)
    : CreditsRepository{
    override suspend fun getMovieCredits(
        movieId: Int,
        language: String
    ): MovieCredits {
        val dto = remoteMovieCreditsDataSource.getMovieCredits(movieId, language)
        return dto.toDomain()
    }
}