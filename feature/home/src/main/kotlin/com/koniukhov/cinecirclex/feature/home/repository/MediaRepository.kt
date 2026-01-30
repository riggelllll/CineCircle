package com.koniukhov.cinecirclex.feature.home.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.koniukhov.cinecirclex.core.common.model.MediaListType
import com.koniukhov.cinecirclex.core.domain.model.MediaItem
import com.koniukhov.cinecirclex.core.domain.usecase.GetAiringTodayTvSeriesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetMoviesByGenreUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetNowPlayingMoviesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetOnAirTvSeriesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetPopularMoviesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetPopularTvSeriesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTopRatedMoviesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTopRatedTvSeriesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTrendingMoviesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTrendingTvSeriesUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetTvSeriesByGenreUseCase
import com.koniukhov.cinecirclex.core.domain.usecase.GetUpcomingMoviesUseCase
import com.koniukhov.cinecirclex.feature.home.paging.MediaPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val nowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val trendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val topRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val upcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val airingTodayTvSeriesUseCase: GetAiringTodayTvSeriesUseCase,
    private val onTheAirTvSeriesUseCase: GetOnAirTvSeriesUseCase,
    private val trendingTvSeriesUseCase: GetTrendingTvSeriesUseCase,
    private val popularTvSeriesUseCase: GetPopularTvSeriesUseCase,
    private val topRatedTvSeriesUseCase: GetTopRatedTvSeriesUseCase,
    private val moviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val tvSeriesByGenreUseCase: GetTvSeriesByGenreUseCase
) {
    fun getMediaFlow(
        listType: MediaListType,
        language: String,
        genreId: Int? = null
    ): Flow<PagingData<MediaItem>> {
        val useCase: suspend (Int, String) -> List<MediaItem> = when (listType) {
            MediaListType.MOVIES_BY_GENRE -> { page, lang ->
                genreId?.let { moviesByGenreUseCase(it, page, lang) } ?: emptyList()
            }
            MediaListType.TV_SERIES_BY_GENRE -> { page, lang ->
                genreId?.let { tvSeriesByGenreUseCase(it, page, lang) } ?: emptyList()
            }
            MediaListType.NOW_PLAYING_MOVIES -> nowPlayingMoviesUseCase::invoke
            MediaListType.TRENDING_MOVIES -> trendingMoviesUseCase::invoke
            MediaListType.POPULAR_MOVIES -> popularMoviesUseCase::invoke
            MediaListType.TOP_RATED_MOVIES -> topRatedMoviesUseCase::invoke
            MediaListType.UPCOMING_MOVIES -> upcomingMoviesUseCase::invoke
            MediaListType.AIRING_TODAY_TV_SERIES -> airingTodayTvSeriesUseCase::invoke
            MediaListType.ON_THE_AIR_TV_SERIES -> onTheAirTvSeriesUseCase::invoke
            MediaListType.TRENDING_TV_SERIES -> trendingTvSeriesUseCase::invoke
            MediaListType.POPULAR_TV_SERIES -> popularTvSeriesUseCase::invoke
            MediaListType.TOP_RATED_TV_SERIES -> topRatedTvSeriesUseCase::invoke
        }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { MediaPagingSource(useCase, language) }
        ).flow
    }

    companion object{
        const val PAGE_SIZE = 20
    }
}