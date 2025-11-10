package com.koniukhov.cinecircle.feature.ai.recommendations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koniukhov.cinecircle.core.database.dao.RatedMediaDao
import com.koniukhov.cinecircle.feature.ai.recommendations.MediaRepository
import com.koniukhov.cinecircle.feature.ai.recommendations.Recommendation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class MovieRecommendationViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val ratedMediaDao: RatedMediaDao
) : ViewModel() {

    private val _recommendations = MutableStateFlow<List<Recommendation>>(emptyList())
    val recommendations: StateFlow<List<Recommendation>> = _recommendations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasNoRatings = MutableStateFlow(false)
    val hasNoRatings: StateFlow<Boolean> = _hasNoRatings


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
            _isLoading.value = true
            _hasNoRatings.value = false

            val ratedMedias = ratedMediaDao.getAllRatedMedias()

            if (ratedMedias.isEmpty()) {
                _isLoading.value = false
                _hasNoRatings.value = true
                return@launch
            }

            val userRatings = ratedMedias.associate { ratedMedia ->
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
            _isLoading.value = false
        }
    }
}