package com.koniukhov.cinecirclex.core.ui.state

import androidx.paging.PagingData
import com.koniukhov.cinecirclex.core.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

data class MediaPagingUiState(
    val mediaFlow: Flow<PagingData<MediaItem>>? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)