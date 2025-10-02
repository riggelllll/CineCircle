package com.koniukhov.cinecircle.core.data.local

import com.koniukhov.cinecircle.core.database.dao.MediaListDao
import com.koniukhov.cinecircle.core.database.entity.TvSeriesWithGenres
import javax.inject.Inject

class LocalTvSeriesDataSourceImpl @Inject constructor(
    private val mediaListDao: MediaListDao
): LocalTvSeriesDataSource {
    override suspend fun getTvSeriesWithGenres(tvSeriesId: Int): TvSeriesWithGenres? {
        return mediaListDao.getTvSeriesWithGenres(tvSeriesId)
    }
}