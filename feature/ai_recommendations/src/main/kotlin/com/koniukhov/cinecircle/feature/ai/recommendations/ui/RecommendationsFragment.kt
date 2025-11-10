package com.koniukhov.cinecircle.feature.ai.recommendations.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.koniukhov.cinecircle.feature.ai.recommendations.Recommendation
import com.koniukhov.cinecircle.feature.ai.recommendations.viewmodel.MovieRecommendationViewModel
import com.koniukhov.cinecircle.feature.ai_recommendations.databinding.FragmentRecommendationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.collections.forEachIndexed

@AndroidEntryPoint
class RecommendationsFragment : Fragment() {

    private var _binding: FragmentRecommendationsBinding? = null
    private val bindings get() = _binding!!
    private val viewModel: MovieRecommendationViewModel by viewModels()

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return bindings.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        startRecommendationCalculation()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observeLoadingState() }
                launch { observeRecommendations() }
            }
        }
    }

    private suspend fun observeLoadingState() {
        viewModel.isLoading.collectLatest { isLoading ->
            if (isLoading) {
                Timber.d("STATUS: Calculating recommendations...")
            } else {
                Timber.d("STATUS: Calculation completed.")
            }
        }
    }

    private suspend fun observeRecommendations() {
        viewModel.recommendations.collectLatest { recommendations ->
            if (recommendations.isNotEmpty()) {
                logRecommendations(recommendations)
            } else if (!viewModel.isLoading.value) {
                Timber.d("RESULT: Recommendations list is empty.")
            }
        }
    }

    private fun startRecommendationCalculation() {
        val userRatings = mapOf(
            862 to 5.0f,
            8844 to 4.0f,
            278 to 4.5f
        )
        viewModel.generateRecommendations(userRatings, topN = 10)
    }

    private fun logRecommendations(recommendations: List<Recommendation>) {
        recommendations.forEachIndexed { index, reco ->
            Timber.d(
                "${index + 1}. TMDB ID: ${reco.tmdbId} | Similarity: ${"%.4f".format(reco.score)}"
            )
        }
    }
}