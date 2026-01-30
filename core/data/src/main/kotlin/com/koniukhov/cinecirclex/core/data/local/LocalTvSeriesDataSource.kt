package com.koniukhov.cinecirclex.core.data.local

import com.koniukhov.cinecirclex.core.database.entity.TvSeriesWithGenres

interface LocalTvSeriesDataSource {
    suspend fun getTvSeriesWithGenres(tvSeriesId: Int): TvSeriesWithGenres?
}