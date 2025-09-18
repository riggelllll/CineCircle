package com.koniukhov.cinecircle.feature.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.core.domain.usecase.GetSearchedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchedMoviesUseCase: GetSearchedMoviesUseCase,
    @LanguageCode
    private val languageCode: String
) : ViewModel() {
    fun loadData(){
        viewModelScope.launch {
            val data = getSearchedMoviesUseCase("Harry Potter", 1, languageCode)
            Timber.d(data.toString())
        }
    }
}