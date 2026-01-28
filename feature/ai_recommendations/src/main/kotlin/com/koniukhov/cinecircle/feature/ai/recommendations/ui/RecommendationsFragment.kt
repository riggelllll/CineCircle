package com.koniukhov.cinecircle.feature.ai.recommendations.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.common.navigation.navigateToMovieDetails
import com.koniukhov.cinecircle.core.common.navigation.navigateToTvSeriesDetails
import com.koniukhov.cinecircle.core.ui.adapter.MediaListAdapter
import com.koniukhov.cinecircle.core.ui.base.BaseFragment
import com.koniukhov.cinecircle.core.ui.utils.GridSpacingItemDecoration
import com.koniukhov.cinecircle.feature.ai.recommendations.viewmodel.MovieRecommendationViewModel
import com.koniukhov.cinecircle.feature.ai_recommendations.R
import com.koniukhov.cinecircle.feature.ai_recommendations.databinding.FragmentRecommendationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RecommendationsFragment : BaseFragment<FragmentRecommendationsBinding, MovieRecommendationViewModel>() {

    override val viewModel: MovieRecommendationViewModel by viewModels()

    private lateinit var mediaAdapter: MediaListAdapter
    private var isErrorDialogShowing = false

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
            launch { observeLoadingProgress() }
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
                binding.loadingStateLayout.visibility = View.VISIBLE
                binding.emptyStateLayout.visibility = View.GONE
                binding.recommendationsRecyclerView.visibility = View.GONE
                binding.recommendationsTitle.visibility = View.GONE
                Timber.d("STATUS: Calculating recommendations...")
            } else {
                binding.loadingStateLayout.visibility = View.GONE
                Timber.d("STATUS: Calculation completed.")

                if (viewModel.recommendedMedia.value.isEmpty() && !viewModel.hasNoRatings.value) {
                    binding.recommendationsRecyclerView.visibility = View.GONE
                    binding.recommendationsTitle.visibility = View.GONE
                    showErrorDialog()
                    Timber.d("RESULT: Recommendations list is empty.")
                }
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
            } else {
                binding.noRatingsTitle.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun observeLoadingProgress() {
        viewModel.totalCount.collectLatest { total ->
            viewModel.loadedCount.collectLatest { loaded ->
                if (viewModel.isLoading.value) {
                    if (total > 0) {
                        binding.loadingProgressCount.visibility = View.VISIBLE
                        binding.loadingProgressCount.text = getString(R.string.loading_progress, loaded, total)
                    } else {
                        binding.loadingProgressCount.visibility = View.GONE
                    }
                } else {
                    binding.loadingProgressCount.visibility = View.GONE
                }
            }
        }
    }

    private fun showErrorDialog() {
        if (isErrorDialogShowing) return

        isErrorDialogShowing = true
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.recommendations_error_title)
            .setMessage(R.string.recommendations_error_message)
            .setCancelable(false)
            .setNegativeButton(R.string.exit) { _, _ ->
                isErrorDialogShowing = false
                requireActivity().finish()
            }
            .setPositiveButton(R.string.retry) { _, _ ->
                isErrorDialogShowing = false
                binding.loadingStateLayout.visibility = View.VISIBLE
                viewModel.generateRecommendationsFromDatabase(topN = 20, forceRefresh = true)
            }
            .setOnDismissListener {
                isErrorDialogShowing = false
            }
            .show()
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