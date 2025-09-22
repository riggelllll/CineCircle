package com.koniukhov.cinecircle.feature.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import timber.log.Timber

class FilteredMovieSource(
    private val filteredMoviesUseCase: suspend (
        page: Int,
        language: String,
        sortBy: String,
        year: Int?,
        releaseDateGte: String?,
        releaseDateLte: String?,
        minVoteAverage: Float?,
        maxVoteAverage: Float?,
        minVoteCount: Int?,
        maxVoteCount: Int?,
        withOriginCountry: String?,
        withOriginalLanguage: String?,
        withGenres: String?,
        withoutGenres: String?) -> List<MediaItem>,
    private val language: String,
    private val sortBy: String,
    private val year: Int? = null,
    private val releaseDateGte: String? = null,
    private val releaseDateLte: String? = null,
    private val minVoteAverage: Float? = null,
    private val maxVoteAverage: Float? = null,
    private val minVoteCount: Int? = null,
    private val maxVoteCount: Int? = null,
    private val withOriginCountry: String? = null,
    private val withOriginalLanguage: String? = null,
    private val withGenres: String? = null,
    private val withoutGenres: String? = null

) : PagingSource<Int, MediaItem>()  {

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
        val page = params.key ?: 1
        return try {
            val items = filteredMoviesUseCase(
                page,
                language,
                sortBy,
                year,
                releaseDateGte,
                releaseDateLte,
                minVoteAverage,
                maxVoteAverage,
                minVoteCount,
                maxVoteCount,
                withOriginCountry,
                withOriginalLanguage,
                withGenres,
                withoutGenres
            )

            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Timber.d(e)
            LoadResult.Error(e)
        }
    }
}