package com.koniukhov.cinecirclex.core.data.local

import com.koniukhov.cinecirclex.core.database.dao.MediaListDao
import com.koniukhov.cinecirclex.core.database.entity.MovieWithGenres
import javax.inject.Inject

class LocalMoviesDataSourceImpl @Inject constructor(
    private val mediaListDao: MediaListDao
) : LocalMoviesDataSource {
    override suspend fun getMovieWithGenres(movieId: Int): MovieWithGenres? {
        return mediaListDao.getMovieWithGenres(movieId)
    }
}