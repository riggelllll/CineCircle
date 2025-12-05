package com.koniukhov.cinecircle.feature.ai.recommendations.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.common.navigation.navigateToMovieDetails
import com.koniukhov.cinecircle.core.common.navigation.navigateToTvSeriesDetails
import com.koniukhov.cinecircle.core.ui.adapter.MediaListAdapter
import com.koniukhov.cinecircle.core.ui.base.BaseFragment
import com.koniukhov.cinecircle.core.ui.utils.GridSpacingItemDecoration
import com.koniukhov.cinecircle.feature.ai.recommendations.viewmodel.MovieRecommendationViewModel
import com.koniukhov.cinecircle.feature.ai_recommendations.databinding.FragmentRecommendationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RecommendationsFragment : BaseFragment<FragmentRecommendationsBinding, MovieRecommendationViewModel>() {

    override val viewModel: MovieRecommendationViewModel by viewModels()

    private lateinit var mediaAdapter: MediaListAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecommendationsBinding {
        return FragmentRecommendationsBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupMediaAdapter()
        setupRecyclerView()
    }

    override fun observeViewModel() {
        launchWhenStarted {
            launch { observeLoadingState() }
            launch { observeRecommendedMovies() }
            launch { observeEmptyState() }
        }
    }

    override fun setupViews() {
        super.setupViews()
        startRecommendationCalculation()
    }

    private fun setupMediaAdapter() {
        mediaAdapter = MediaListAdapter { mediaId, mediaType ->
            navigateToDetails(mediaId, mediaType)
        }
    }


    private fun setupRecyclerView() {
        binding.recommendationsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(com.koniukhov.cinecircle.core.design.R.dimen.grid_spacing)
        binding.recommendationsRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(2, spacing, true)
        )
        binding.recommendationsRecyclerView.adapter = mediaAdapter
    }

    private suspend fun observeLoadingState() {
        viewModel.isLoading.collectLatest { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.emptyStateLayout.visibility = View.GONE
                binding.recommendationsRecyclerView.visibility = View.GONE
                binding.recommendationsTitle.visibility = View.GONE
                Timber.d("STATUS: Calculating recommendations...")
            } else {
                binding.progressBar.visibility = View.GONE
                Timber.d("STATUS: Calculation completed.")
            }
        }
    }

    private suspend fun observeRecommendedMovies() {
        viewModel.recommendedMedia.collectLatest { mediaItems ->
            if (mediaItems.isNotEmpty()) {
                binding.recommendationsRecyclerView.visibility = View.VISIBLE
                binding.recommendationsTitle.visibility = View.VISIBLE
                binding.emptyStateLayout.visibility = View.GONE
                mediaAdapter.setMediaItems(mediaItems)
                Timber.d("Displaying ${mediaItems.size} recommended media items")
            } else if (!viewModel.isLoading.value && !viewModel.hasNoRatings.value) {
                binding.recommendationsRecyclerView.visibility = View.GONE
                binding.recommendationsTitle.visibility = View.GONE
                Timber.d("RESULT: Recommendations list is empty.")
            }
        }
    }

    private suspend fun observeEmptyState() {
        viewModel.hasNoRatings.collectLatest { hasNoRatings ->
            if (hasNoRatings) {
                binding.emptyStateLayout.visibility = View.VISIBLE
                binding.recommendationsRecyclerView.visibility = View.GONE
                Timber.d("STATUS: No ratings found in database.")
            } else {
                binding.emptyStateLayout.visibility = View.GONE
            }
        }
    }

    private fun startRecommendationCalculation() {
        viewModel.generateRecommendationsFromDatabase(topN = 20)
    }

    private fun navigateToDetails(mediaId: Int, mediaType: MediaType) {
        when (mediaType) {
            MediaType.MOVIE -> findNavController().navigateToMovieDetails(mediaId)
            MediaType.TV_SERIES -> findNavController().navigateToTvSeriesDetails(mediaId)
        }
    }

    override fun onDestroyView() {
        binding.recommendationsRecyclerView.adapter = null
        super.onDestroyView()
    }
}