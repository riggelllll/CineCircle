package com.koniukhov.cinecircle.feature.home.ui.state

import androidx.paging.PagingData
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

data class MediaUiState(
    val mediaFlow: Flow<PagingData<MediaItem>>? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)