package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetPopularTvSeriesUseCase @Inject constructor(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(page: Int) = repository.getPopularTvSeries(page)
}