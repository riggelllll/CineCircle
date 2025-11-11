package com.koniukhov.cinecircle.feature.ai.recommendations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID
import com.koniukhov.cinecircle.core.data.di.LanguageCode
import com.koniukhov.cinecircle.core.database.dao.RatedMediaDao
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.repository.MoviesRepository
import com.koniukhov.cinecircle.core.domain.repository.TvSeriesRepository
import com.koniukhov.cinecircle.feature.ai.recommendations.MediaRepository
import com.koniukhov.cinecircle.feature.ai.recommendations.Recommendation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class MovieRecommendationViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val ratedMediaDao: RatedMediaDao,
    private val moviesRepository: MoviesRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    @LanguageCode
    private val languageCode: String,
) : ViewModel() {

    private val _recommendations = MutableStateFlow<List<Recommendation>>(emptyList())
    val recommendations: StateFlow<List<Recommendation>> = _recommendations

    private val _recommendedMedia = MutableStateFlow<List<MediaItem>>(emptyList())
    val recommendedMedia: StateFlow<List<MediaItem>> = _recommendedMedia

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasNoRatings = MutableStateFlow(false)
    val hasNoRatings: StateFlow<Boolean> = _hasNoRatings

    private var isRecommendationsCached = false
    private var cachedRatedMediasCount = 0


    private fun calculateUserVector(userRatings: Map<Int, Float>): FloatArray? {
        val userVectors = mutableListOf<FloatArray>()
        val weights = mutableListOf<Float>()

        val allMovies = repository.getAllMovieVectors()
        val tmdbToVectorMap = allMovies.associateBy { it.tmdbId }

        for ((tmdbId, rating) in userRatings) {
            val movieVector = tmdbToVectorMap[tmdbId]

            if (movieVector != null) {
                userVectors.add(movieVector.vector)
                weights.add(rating)
            }
        }

        if (userVectors.isEmpty()) return null

        val dim = userVectors.first().size
        val userVector = FloatArray(dim)
        var totalWeight = 0f

        for (i in userVectors.indices) {
            val w = weights[i]
            totalWeight += w
            for (j in 0 until dim) {
                userVector[j] += userVectors[i][j] * w
            }
        }

        if (totalWeight > 0) {
            for (j in 0 until dim) {
                userVector[j] /= totalWeight
            }
        }
        return userVector
    }

    private fun cosineSimilarity(vecA: FloatArray, vecB: FloatArray): Float {
        var dotProduct = 0f
        var normA = 0f
        var normB = 0f
        val epsilon = 1e-8f

        for (i in vecA.indices) {
            dotProduct += vecA[i] * vecB[i]
            normA += vecA[i] * vecA[i]
            normB += vecB[i] * vecB[i]
        }

        val denominator = sqrt(normA) * sqrt(normB)

        return if (denominator < epsilon) 0f else dotProduct / denominator
    }


    fun generateRecommendations(userRatings: Map<Int, Float>, topN: Int = 10) {
        if (userRatings.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            repository.initialize()

            val userVector = calculateUserVector(userRatings)
            if (userVector == null) {
                _isLoading.value = false
                return@launch
            }

            val allMovies = repository.getAllMovieVectors()
            val userRatedTmdbIds = userRatings.keys

            val scores = allMovies.mapNotNull { movie ->
                if (userRatedTmdbIds.contains(movie.tmdbId)) null else {
                    val sim = cosineSimilarity(userVector, movie.vector)
                    Recommendation(movie.tmdbId, sim)
                }
            }

            val topRecommendations = scores
                .sortedByDescending { it.score }
                .take(topN)

            _recommendations.value = topRecommendations
            _isLoading.value = false
        }
    }

    fun generateRecommendationsFromDatabase(topN: Int = 10) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentRatedMedias = ratedMediaDao.getAllRatedMedias()
            if (isRecommendationsCached && 
                cachedRatedMediasCount == currentRatedMedias.size &&
                _recommendedMedia.value.isNotEmpty()) {
                Timber.d("Using cached recommendations (Count: ${_recommendedMedia.value.size})")
                return@launch
            }

            _isLoading.value = true
            _hasNoRatings.value = false
            _recommendedMedia.value = emptyList()

            if (currentRatedMedias.isEmpty()) {
                _isLoading.value = false
                _hasNoRatings.value = true
                isRecommendationsCached = true
                cachedRatedMediasCount = 0
                return@launch
            }

            cachedRatedMediasCount = currentRatedMedias.size

            val userRatings = currentRatedMedias.associate { ratedMedia ->
                ratedMedia.mediaId.toInt() to ratedMedia.rating
            }

            repository.initialize()

            val userVector = calculateUserVector(userRatings)
            if (userVector == null) {
                _isLoading.value = false
                return@launch
            }

            val allMovies = repository.getAllMovieVectors()
            val userRatedTmdbIds = userRatings.keys

            val scores = allMovies.mapNotNull { movie ->
                if (userRatedTmdbIds.contains(movie.tmdbId)) null else {
                    val sim = cosineSimilarity(userVector, movie.vector)
                    Recommendation(movie.tmdbId, sim)
                }
            }

            val topRecommendations = scores
                .sortedByDescending { it.score }
                .take(topN)

            _recommendations.value = topRecommendations

            val mediaList = mutableListOf<MediaItem>()
            topRecommendations.forEach { recommendation ->
                try {
                    try {
                        val movieDetails = moviesRepository.getMovieDetails(
                            movieId = recommendation.tmdbId,
                            language = languageCode
                        )
                        mediaList.add(
                            Movie(
                                id = movieDetails.id,
                                title = movieDetails.title,
                                posterPath = movieDetails.posterPath,
                                backdropPath = movieDetails.backdropPath,
                                overview = movieDetails.overview,
                                releaseDate = movieDetails.releaseDate,
                                voteAverage = movieDetails.voteAverage,
                                voteCount = movieDetails.voteCount,
                                genreIds = movieDetails.genres.map { it.id },
                                originalLanguage = movieDetails.originalLanguage,
                                originalTitle = movieDetails.originalTitle,
                                popularity = movieDetails.popularity,
                                adult = movieDetails.adult,
                                video = movieDetails.video
                            )
                        )
                        Timber.d("Loaded movie: ${movieDetails.title} (${recommendation.tmdbId})")
                    } catch (movieError: Exception) {
                        try {
                            val tvSeriesDetails = tvSeriesRepository.getTvSeriesDetails(
                                id = recommendation.tmdbId,
                                language = languageCode
                            )
                            mediaList.add(
                                TvSeries(
                                    id = tvSeriesDetails.id,
                                    title = tvSeriesDetails.name,
                                    posterPath = tvSeriesDetails.posterPath,
                                    backdropPath = tvSeriesDetails.backdropPath,
                                    overview = tvSeriesDetails.overview,
                                    firstAirDate = tvSeriesDetails.firstAirDate,
                                    voteAverage = tvSeriesDetails.voteAverage,
                                    voteCount = tvSeriesDetails.voteCount,
                                    genreIds = tvSeriesDetails.genres.map { it.id },
                                    originalLanguage = tvSeriesDetails.originalLanguage,
                                    originCountry = tvSeriesDetails.originCountry,
                                    originalName = tvSeriesDetails.originalName,
                                    popularity = tvSeriesDetails.popularity,
                                    adult = tvSeriesDetails.adult
                                )
                            )
                            Timber.d("Loaded TV series: ${tvSeriesDetails.name} (${recommendation.tmdbId})")
                        } catch (tvError: Exception) {
                            Timber.e(movieError, "Failed to load media details for TMDB ID: ${recommendation.tmdbId}")
                            Timber.e(tvError, "Also failed as TV series")
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Unexpected error loading media for TMDB ID: ${recommendation.tmdbId}")
                }
            }
            _recommendedMedia.value = mediaList.filter { it.id != INVALID_ID }
            isRecommendationsCached = true
            _isLoading.value = false
            Timber.d("Recommendations loaded and cached: ${mediaList.size} items")
        }
    }

    fun invalidateCache() {
        isRecommendationsCached = false
        cachedRatedMediasCount = 0
        Timber.d("Cache invalidated")
    }
}