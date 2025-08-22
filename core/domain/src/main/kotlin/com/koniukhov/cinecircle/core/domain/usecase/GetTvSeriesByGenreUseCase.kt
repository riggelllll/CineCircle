package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import javax.inject.Inject

class GetTvSeriesByGenreUseCase @Inject constructor(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(genreId: Int, page: Int, language: String): List<TvSeries> {
        return repository.getTvSeriesByGenre(genreId, page, language)
    }
}