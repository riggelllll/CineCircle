package com.koniukhov.cinecircle.feature.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.koniukhov.cinecircle.core.common.util.getLocalizedCountryMap
import com.koniukhov.cinecircle.core.common.util.getLocalizedLanguageMap
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieGenresUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesGenresUseCase
import com.koniukhov.cinecircle.feature.search.model.MovieFilterParams
import com.koniukhov.cinecircle.feature.search.model.TvSeriesFilterParams
import com.koniukhov.cinecircle.feature.search.repository.FilteredMediaRepository
import com.koniukhov.cinecircle.feature.search.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val filteredMediaRepository: FilteredMediaRepository,
    private val movieGenresUseCase: GetMovieGenresUseCase,
    private val tvSeriesGenresUseCase: GetTvSeriesGenresUseCase,
    @LanguageCode
    private val languageCode: String
) : ViewModel() {
    var movieGenres: MutableStateFlow<Map<Int, String>> = MutableStateFlow(emptyMap())
        private set
    var tvSeriesGenres: MutableStateFlow<Map<Int, String>> = MutableStateFlow(emptyMap())
        private set
    var languages: Map<String, String> = emptyMap()
        private set
    var countries: Map<String, String> = emptyMap()
        private set
    private val searchQuery = MutableStateFlow("")
    val searchPagingDataFlow = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { q ->
            val query = q.trim()
            if (query.isBlank()) flowOf(PagingData.empty())
            else searchRepository.getSearchFlow(query, languageCode)
        }
        .cachedIn(viewModelScope)

    private var _moviesFilterParamsState = MutableStateFlow<MovieFilterParams?>(null)
    val moviesFilterParamsState = _moviesFilterParamsState.asStateFlow()

    private var _tvSeriesFilterParamsState = MutableStateFlow<TvSeriesFilterParams?>(null)
    val tvSeriesFilterParamsState = _tvSeriesFilterParamsState.asStateFlow()

    val moviesFilterPagingDataFlow = _moviesFilterParamsState
        .flatMapLatest { params ->
            if (params == null) {
                flowOf(PagingData.empty())
            } else {
                filteredMediaRepository.getFilteredMoviesFlow(
                    language = languageCode,
                    sortBy = params.sortBy,
                    year = params.year,
                    releaseDateGte = params.releaseDateGte,
                    releaseDateLte = params.releaseDateLte,
                    minVoteAverage = params.minVoteAverage,
                    maxVoteAverage = params.maxVoteAverage,
                    minVoteCount = params.minVoteCount,
                    maxVoteCount = params.maxVoteCount,
                    withOriginCountry = params.withOriginCountry,
                    withOriginalLanguage = params.withOriginalLanguage,
                    withGenres = params.withGenres,
                    withoutGenres = params.withoutGenres
                )
            }
        }
        .cachedIn(viewModelScope)

    val tvSeriesFilterPagingDataFlow = _tvSeriesFilterParamsState
        .flatMapLatest { params ->
            if (params == null) {
                flowOf(PagingData.empty())
            } else {
                filteredMediaRepository.getFilteredTvSeriesFlow(
                    language = languageCode,
                    sortBy = params.sortBy,
                    year = params.year,
                    firstAirDateGte = params.firstAirDateGte,
                    firstAirDateLte = params.firstAirDateLte,
                    airDateGte = params.airDateGte,
                    airDateLte = params.airDateLte,
                    minVoteAverage = params.minVoteAverage,
                    maxVoteAverage = params.maxVoteAverage,
                    minVoteCount = params.minVoteCount,
                    maxVoteCount = params.maxVoteCount,
                    withOriginCountry = params.withOriginCountry,
                    withOriginalLanguage = params.withOriginalLanguage,
                    withGenres = params.withGenres,
                    withoutGenres = params.withoutGenres
                )
            }
        }.cachedIn(viewModelScope)

    init {
        setUpGenres()
        setupLanguagesAndCountries()
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            searchQuery.emit(query)
        }
    }

    private fun setUpGenres() {
        viewModelScope.launch {
            movieGenres.value = movieGenresUseCase(languageCode).associate { it.id to it.name }
            tvSeriesGenres.value = tvSeriesGenresUseCase(languageCode).associate { it.id to it.name }
        }
    }

    private fun setupLanguagesAndCountries() {
        languages = Locale.getDefault().getLocalizedLanguageMap()
        countries = Locale.getDefault().getLocalizedCountryMap()
    }

     fun selectMovieFilters(params: MovieFilterParams?) {
         resetTvSeriesFilters()
         updateMovieFilters(params)
    }

    private fun resetTvSeriesFilters() {
        viewModelScope.launch { _tvSeriesFilterParamsState.emit(null) }
    }

    private fun updateMovieFilters(params: MovieFilterParams?) {
        viewModelScope.launch { _moviesFilterParamsState.emit(params) }
    }

    fun selectTvSeriesFilters(params: TvSeriesFilterParams?) {
        resetMovieFilters()
        updateTvSeriesFilters(params)
    }

    private fun resetMovieFilters() {
        viewModelScope.launch { _moviesFilterParamsState.emit(null) }
    }

    private fun updateTvSeriesFilters(params: TvSeriesFilterParams?) {
        viewModelScope.launch { _tvSeriesFilterParamsState.emit(params) }
    }
}