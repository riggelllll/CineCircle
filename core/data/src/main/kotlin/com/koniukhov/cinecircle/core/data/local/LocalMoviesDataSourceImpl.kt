package com.koniukhov.cinecircle.core.data.local

import com.koniukhov.cinecircle.core.database.dao.MediaListDao
import com.koniukhov.cinecircle.core.database.entity.MovieWithGenres
import javax.inject.Inject

class LocalMoviesDataSourceImpl @Inject constructor(
    private val mediaListDao: MediaListDao
) : LocalMoviesDataSource {
    override suspend fun getMovieWithGenres(movieId: Int): MovieWithGenres? {
        return mediaListDao.getMovieWithGenres(movieId)
    }
}