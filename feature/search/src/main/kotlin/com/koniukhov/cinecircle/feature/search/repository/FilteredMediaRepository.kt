package com.koniukhov.cinecircle.feature.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.koniukhov.cinecircle.core.common.sort.MovieSortOption
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.domain.usecase.GetFilteredMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetFilteredTvSeriesUseCase
import com.koniukhov.cinecircle.feature.search.paging.FilteredMovieSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilteredMediaRepository @Inject constructor(
    private val filteredMoviesUseCase: GetFilteredMoviesUseCase,
    private val filteredTvSeriesUseCase: GetFilteredTvSeriesUseCase
) {
    fun getFilteredMoviesFlow(
        language: String,
        sortBy: String?,
        year: Int?,
        releaseDateGte: String?,
        releaseDateLte: String?,
        minVoteAverage: Float?,
        maxVoteAverage: Float?,
        minVoteCount: Int?,
        maxVoteCount: Int?,
        withOriginCountry: String?,
        withOriginalLanguage: String?,
        withGenres: String?,
        withoutGenres: String?
    ): Flow<PagingData<MediaItem>> {
        val moviesUseCase = filteredMoviesUseCase::invoke

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                FilteredMovieSource(
                    filteredMoviesUseCase = moviesUseCase,
                    language = language,
                    sortBy = sortBy ?: MovieSortOption.POPULARITY_DESC.apiValue,
                    year = year,
                    releaseDateGte = releaseDateGte,
                    releaseDateLte = releaseDateLte,
                    minVoteAverage = minVoteAverage,
                    maxVoteAverage = maxVoteAverage,
                    minVoteCount = minVoteCount,
                    maxVoteCount = maxVoteCount,
                    withOriginCountry = withOriginCountry,
                    withOriginalLanguage = withOriginalLanguage,
                    withGenres = withGenres,
                    withoutGenres = withoutGenres
                )
            }
        ).flow
    }

    fun getFilteredTvSeriesFlow(
        language: String,
        sortBy: String?,
        airDateGte: String?,
        airDateLte: String?,
        year: Int?,
        firstAirDateGte: String?,
        firstAirDateLte: String?,
        minVoteAverage: Float?,
        maxVoteAverage: Float?,
        minVoteCount: Int?,
        maxVoteCount: Int?,
        withOriginCountry: String?,
        withOriginalLanguage: String?,
        withGenres: String?,
        withoutGenres: String?
    ) : Flow<PagingData<MediaItem>> {
        val tvSeriesUseCase = filteredTvSeriesUseCase::invoke

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                com.koniukhov.cinecircle.feature.search.paging.FilteredTvSeriesSource(
                    filteredTvSeriesUseCase = tvSeriesUseCase,
                    language = language,
                    sortBy = sortBy ?: MovieSortOption.POPULARITY_DESC.apiValue,
                    airDateGte = airDateGte,
                    airDateLte = airDateLte,
                    year = year,
                    firstAirDateGte = firstAirDateGte,
                    firstAirDateLte = firstAirDateLte,
                    minVoteAverage = minVoteAverage,
                    maxVoteAverage = maxVoteAverage,
                    minVoteCount = minVoteCount,
                    maxVoteCount = maxVoteCount,
                    withOriginCountry = withOriginCountry,
                    withOriginalLanguage = withOriginalLanguage,
                    withGenres = withGenres,
                    withoutGenres = withoutGenres
                )
            }
        ).flow
    }

    companion object{
        const val PAGE_SIZE = 20
    }
}