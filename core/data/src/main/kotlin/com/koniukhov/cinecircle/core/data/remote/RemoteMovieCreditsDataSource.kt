package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.CreditsDatasource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.MovieCreditsDto
import javax.inject.Inject

class RemoteMovieCreditsDataSource @Inject constructor(private val api: TMDBApi) : CreditsDatasource {
    override suspend fun getMovieCredits(
        movieId: Int,
        language: String
    ): MovieCreditsDto {
        return api.getMovieCredits(movieId, BuildConfig.API_KEY, language).body() ?: MovieCreditsDto.empty()
    }
}