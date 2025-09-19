package com.koniukhov.cinecircle.feature.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.feature.search.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    @LanguageCode
    private val languageCode: String
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    val pagingDataFlow = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { q ->
            val query = q.trim()
            if (query.isBlank()) flowOf(PagingData.empty())
            else repository.getSearchFlow(query, languageCode)
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            searchQuery.emit(query)
        }
    }
}