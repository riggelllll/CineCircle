package com.koniukhov.cinecirclex.core.domain.repository

import com.koniukhov.cinecirclex.core.domain.model.ContentRating

interface ContentRatingsRepository {
    suspend fun getTvSeriesContentRatings(tvSeriesId: Int): List<ContentRating>
}