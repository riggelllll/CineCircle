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
                launch { observeEmptyState() }
            }
        }
    }

    private suspend fun observeLoadingState() {
        viewModel.isLoading.collectLatest { isLoading ->
            bindings.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                bindings.emptyStateLayout.visibility = View.GONE
                bindings.recommendationsRecyclerView.visibility = View.GONE
                Timber.d("STATUS: Calculating recommendations...")
            } else {
                Timber.d("STATUS: Calculation completed.")
            }
        }
    }

    private suspend fun observeRecommendations() {
        viewModel.recommendations.collectLatest { recommendations ->
            if (recommendations.isNotEmpty()) {
                bindings.recommendationsRecyclerView.visibility = View.VISIBLE
                bindings.emptyStateLayout.visibility = View.GONE
                logRecommendations(recommendations)
            } else if (!viewModel.isLoading.value && !viewModel.hasNoRatings.value) {
                bindings.recommendationsRecyclerView.visibility = View.GONE
                Timber.d("RESULT: Recommendations list is empty.")
            }
        }
    }

    private suspend fun observeEmptyState() {
        viewModel.hasNoRatings.collectLatest { hasNoRatings ->
            if (hasNoRatings) {
                bindings.emptyStateLayout.visibility = View.VISIBLE
                bindings.recommendationsRecyclerView.visibility = View.GONE
                Timber.d("STATUS: No ratings found in database.")
            } else {
                bindings.emptyStateLayout.visibility = View.GONE
            }
        }
    }

    private fun startRecommendationCalculation() {
        viewModel.generateRecommendationsFromDatabase(topN = 10)
    }

    private fun logRecommendations(recommendations: List<Recommendation>) {
        recommendations.forEachIndexed { index, reco ->
            Timber.d(
                "${index + 1}. TMDB ID: ${reco.tmdbId} | Similarity: ${"%.4f".format(reco.score)}"
            )
        }
    }
}