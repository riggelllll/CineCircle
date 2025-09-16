package com.koniukhov.cinecircle.feature.lists.viewmodel

import androidx.lifecycle.ViewModel
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.usecase.GetMovieDetailsUseCase
import com.koniukhov.cinecircle.core.domain.usecase.GetTvSeriesDetailsUseCase
import com.koniukhov.cinecircle.core.database.repository.MediaListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectionContentViewModel @Inject constructor(
    private val mediaListRepository: MediaListRepository,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getTvSeriesDetailsUseCase: GetTvSeriesDetailsUseCase,
    @LanguageCode
    private val languageCode: String
) : ViewModel() {

    fun getCollectionContent(collectionId: Long): Flow<List<MediaItem>> {
        val moviesFlow = mediaListRepository.getMoviesInList(collectionId)
        val tvSeriesFlow = mediaListRepository.getTvSeriesInList(collectionId)

        return combine(moviesFlow, tvSeriesFlow) { movieIds, tvIds ->
            val movies = movieIds.mapNotNull { id ->
                try {
                    val movieDetails = getMovieDetailsUseCase(id, languageCode)
                    Movie(
                        adult = movieDetails.adult,
                        backdropPath = movieDetails.backdropPath,
                        genreIds = movieDetails.genres.map { it.id },
                        id = movieDetails.id,
                        originalLanguage = movieDetails.originalLanguage,
                        originalTitle = movieDetails.originalTitle,
                        overview = movieDetails.overview,
                        popularity = movieDetails.popularity,
                        posterPath = movieDetails.posterPath,
                        releaseDate = movieDetails.releaseDate,
                        title = movieDetails.title,
                        video = movieDetails.video,
                        voteAverage = movieDetails.voteAverage,
                        voteCount = movieDetails.voteCount
                    )
                } catch (e: Exception) {
                    Timber.d(e)
                    null
                }
            }

            val tvSeries = tvIds.mapNotNull { id ->
                try {
                    val tvDetails = getTvSeriesDetailsUseCase(id, languageCode)
                    TvSeries(
                        adult = tvDetails.adult,
                        backdropPath = tvDetails.backdropPath,
                        genreIds = tvDetails.genres.map { it.id },
                        id = tvDetails.id,
                        originCountry = tvDetails.originCountry,
                        originalLanguage = tvDetails.originalLanguage,
                        originalName = tvDetails.originalName,
                        overview = tvDetails.overview,
                        popularity = tvDetails.popularity,
                        posterPath = tvDetails.posterPath,
                        firstAirDate = tvDetails.firstAirDate,
                        title = tvDetails.name,
                        voteAverage = tvDetails.voteAverage,
                        voteCount = tvDetails.voteCount
                    )
                } catch (e: Exception) {
                    Timber.d(e)
                    null
                }
            }

            movies + tvSeries
        }
    }
}