package com.koniukhov.cinecircle.feature.search.viewmodel

import android.R.attr.data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.core.domain.usecase.GetSearchedMoviesUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetSearchedTvSeriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchedMoviesUseCase: GetSearchedMoviesUseCase,
    private val getSearchedTvSeriesUseCase: GetSearchedTvSeriesUseCase,
    @LanguageCode
    private val languageCode: String
) : ViewModel() {
    fun loadData(){
        viewModelScope.launch {
            val movies = getSearchedMoviesUseCase("Harry Potter", 1, languageCode)
            val series = getSearchedTvSeriesUseCase("Breaking Bad", 1, languageCode)
            Timber.d(series.toString())
        }
    }
}