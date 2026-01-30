package com.koniukhov.cinecirclex.core.data.local

import com.koniukhov.cinecirclex.core.database.entity.MovieWithGenres

interface LocalMoviesDataSource {
    suspend fun getMovieWithGenres(movieId: Int): MovieWithGenres?
}