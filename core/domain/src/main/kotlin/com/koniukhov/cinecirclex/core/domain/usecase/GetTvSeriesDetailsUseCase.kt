package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetTvSeriesDetailsUseCase @Inject constructor(private val repository: TvSeriesRepository ) {
    suspend operator fun invoke(id: Int, language: String) = repository.getTvSeriesDetails(id, language)
}