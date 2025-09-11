package com.koniukhov.cinecircle.core.domain.repository

import com.koniukhov.cinecircle.core.domain.model.ContentRating

interface ContentRatingsRepository {
    suspend fun getTvSeriesContentRatings(tvSeriesId: Int): List<ContentRating>
}