package com.koniukhov.cinecircle.core.data.local

import com.koniukhov.cinecircle.core.database.entity.TvSeriesWithGenres

interface LocalTvSeriesDataSource {
    suspend fun getTvSeriesWithGenres(tvSeriesId: Int): TvSeriesWithGenres?
}