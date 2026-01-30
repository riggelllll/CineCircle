package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetSimilarTvSeriesUseCase @Inject constructor(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(tvSeriesId: Int, languageCode: String, page: Int) =
        repository.getSimilarTvSeries(tvSeriesId, page, languageCode)
}