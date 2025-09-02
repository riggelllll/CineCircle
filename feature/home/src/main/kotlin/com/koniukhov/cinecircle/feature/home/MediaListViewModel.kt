package com.koniukhov.cinecircle.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.koniukhov.cinecircle.core.common.model.MediaListType
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.feature.home.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaListViewModel @Inject constructor(
    private val repository: MediaRepository,
    @LanguageCode
    private val languageCode: String
) : ViewModel() {
    private val _uiState: MutableStateFlow<MediaUiState> = MutableStateFlow(MediaUiState())
    val uiState: StateFlow<MediaUiState> get() = _uiState

    fun loadMedia(listType: MediaListType, genreId: Int? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val mediaFlow = repository.getMediaFlow(listType, languageCode, genreId).cachedIn(viewModelScope)
                _uiState.value = _uiState.value.copy(
                    mediaFlow = mediaFlow,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}