package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetAiringTodayTvSeriesUseCase @Inject constructor(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(page: Int, language: String) = repository.getAiringTodayTvSeries(page, language)
}