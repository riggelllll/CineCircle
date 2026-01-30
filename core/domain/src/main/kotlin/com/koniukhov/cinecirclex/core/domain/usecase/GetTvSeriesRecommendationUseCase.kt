package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetTvSeriesRecommendationUseCase @Inject constructor(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(tvSeriesId: Int, languageCode: String, page: Int) =
        repository.getTvSeriesRecommendations(tvSeriesId, page, languageCode)
}