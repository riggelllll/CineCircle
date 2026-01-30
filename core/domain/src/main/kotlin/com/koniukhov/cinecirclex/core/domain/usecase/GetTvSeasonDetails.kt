package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetTvSeasonDetails @Inject constructor(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(tvSeriesId: Int, seasonNumber: Int, languageCode: String) =
        repository.getTvSeasonDetails(tvSeriesId, seasonNumber, languageCode)
}