package com.koniukhov.cinecirclex.feature.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.koniukhov.cinecirclex.core.domain.model.MediaItem
import com.koniukhov.cinecirclex.core.domain.usecase.GetSearchedMoviesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetSearchedTvSeriesUseCase
import com.koniukhov.cinecirclex.feature.search.paging.SearchPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val getSearchedMoviesUseCase: GetSearchedMoviesUseCase,
    private val getSearchedTvSeriesUseCase: GetSearchedTvSeriesUseCase
) {

    fun getSearchFlow(
        query: String,
        language: String
    ): Flow<PagingData<MediaItem>> {
        val moviesUseCase = getSearchedMoviesUseCase::invoke
        val tvSeriesUseCase = getSearchedTvSeriesUseCase::invoke

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = INITIAL_LOAD_SIZE),
            pagingSourceFactory = {
                SearchPagingSource(
                    query = query,
                    moviesUseCase = moviesUseCase,
                    tvSeriesUseCase = tvSeriesUseCase,
                    language = language
                )
            }
        ).flow
    }

    companion object{
        const val PAGE_SIZE = 20
        const val INITIAL_LOAD_SIZE = 40
    }
}