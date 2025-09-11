package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetSimilarTvSeriesUseCase @Inject constructor(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(tvSeriesId: Int, languageCode: String, page: Int) =
        repository.getSimilarTvSeries(tvSeriesId, page, languageCode)
}