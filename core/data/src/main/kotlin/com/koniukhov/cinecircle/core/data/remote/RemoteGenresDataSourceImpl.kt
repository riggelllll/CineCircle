package com.koniukhov.cinecircle.core.data.remote

import com.koniukhov.cinecircle.core.data.BuildConfig
import com.koniukhov.cinecircle.core.domain.datasource.GenresDataSource
import com.koniukhov.cinecircle.core.network.api.TMDBApi
import com.koniukhov.cinecircle.core.network.model.GenreDto
import javax.inject.Inject

class RemoteGenresDataSourceImpl @Inject constructor(
    private val api: TMDBApi
) : GenresDataSource{
    override suspend fun getMoviesGenreList(language: String): List<GenreDto> {
        return api.getMovieGenres(BuildConfig.API_KEY, language).body()?.genres ?: emptyList()
    }

    override suspend fun getTvSeriesGenreList(language: String): List<GenreDto> {
        return api.getTvSeriesGenres(BuildConfig.API_KEY, language).body()?.genres ?: emptyList()
    }
}