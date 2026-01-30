package com.koniukhov.cinecirclex.core.data.remote

import com.koniukhov.cinecirclex.core.data.BuildConfig
import com.koniukhov.cinecirclex.core.network.api.TMDBApi
import com.koniukhov.cinecirclex.core.network.model.GenreDto
import javax.inject.Inject

class RemoteGenresDataSourceImpl @Inject constructor(
    private val api: TMDBApi
) : RemoteGenresDataSource{
    override suspend fun getMoviesGenreList(language: String): List<GenreDto> {
        return api.getMovieGenres(BuildConfig.API_KEY, language).body()?.genres ?: emptyList()
    }

    override suspend fun getTvSeriesGenreList(language: String): List<GenreDto> {
        return api.getTvSeriesGenres(BuildConfig.API_KEY, language).body()?.genres ?: emptyList()
    }
}