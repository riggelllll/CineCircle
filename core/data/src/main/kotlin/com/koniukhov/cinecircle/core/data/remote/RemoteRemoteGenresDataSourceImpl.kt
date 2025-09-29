package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.RemoteGenresDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.GenreDto
import javax.inject.Inject

class RemoteRemoteGenresDataSourceImpl @Inject constructor(
    private val api: TMDBApi
) : RemoteGenresDataSource{
    override suspend fun getMoviesGenreList(language: String): List<GenreDto> {
        return api.getMovieGenres(BuildConfig.API_KEY, language).body()?.genres ?: emptyList()
    }

    override suspend fun getTvSeriesGenreList(language: String): List<GenreDto> {
        return api.getTvSeriesGenres(BuildConfig.API_KEY, language).body()?.genres ?: emptyList()
    }
}