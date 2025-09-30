package com.koniukhov.cinecircle.core.data.local

import com.koniukhov.cinecircle.core.database.entity.MovieWithGenres

interface LocalMoviesDataSource {
    suspend fun getMovieWithGenres(movieId: Int): MovieWithGenres?
}