package com.koniukhov.cinecircle.feature.lists.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.database.model.MediaListWithCount
import com.koniukhov.cinecircle.core.database.repository.MediaListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val mediaListRepository: MediaListRepository
) : ViewModel() {

    private val _collections = MutableStateFlow<List<MediaListWithCount>>(emptyList())
    val collections: StateFlow<List<MediaListWithCount>> = _collections.asStateFlow()

    init {
        loadCollections()
    }

    private fun loadCollections() {
        viewModelScope.launch {
            try {
                mediaListRepository.getAllLists().collect { lists ->
                    val collectionsWithCount = lists.map { list ->
                        val itemCount = mediaListRepository.getMediaCountInList(list.id)
                        MediaListWithCount(
                            id = list.id,
                            name = list.name,
                            itemCount = itemCount,
                            isDefault = list.isDefault
                        )
                    }
                    _collections.value = collectionsWithCount
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun refreshCollections() {
        loadCollections()
    }
}
