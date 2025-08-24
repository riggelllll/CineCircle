package com.koniukhov.cinecircle.feature.home.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.koniukhov.cinecircle.core.domain.model.MediaItem

class MediaPagingSource(
    private val getMediaPage: suspend (page: Int, language: String) -> List<MediaItem>,
    private val language: String
) : PagingSource<Int, MediaItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
        val page = params.key ?: 1
        return try {
            val items = getMediaPage(page, language)
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}