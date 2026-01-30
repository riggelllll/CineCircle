package com.koniukhov.cinecirclex.feature.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.koniukhov.cinecirclex.core.domain.model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SearchPagingSource(
    private val moviesUseCase: suspend (query: String, page: Int, language: String) -> List<MediaItem>,
    private val tvSeriesUseCase: suspend (query: String, page: Int, language: String) -> List<MediaItem>,
    private val query: String,
    private val language: String
) : PagingSource<Int, MediaItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
        val page = params.key ?: 1
        return try {
            val moviesDeferred = coroutineScope { async(Dispatchers.IO) { moviesUseCase(query, page, language) } }
            val tvSeriesDeferred = coroutineScope { async(Dispatchers.IO) { tvSeriesUseCase(query, page, language) } }

            val movies = moviesDeferred.await()
            val tvSeries = tvSeriesDeferred.await()
            val items = interleaveAlternating(movies, tvSeries)

            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun interleaveAlternating(first: List<MediaItem>, second: List<MediaItem>): List<MediaItem> {
        val result = ArrayList<MediaItem>(first.size + second.size)
        val size = maxOf(first.size, second.size)
        for (i in 0 until size) {
            if (i < first.size) result.add(first[i])
            if (i < second.size) result.add(second[i])
        }
        return result
    }

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}