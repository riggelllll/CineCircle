package com.koniukhov.cinecirclex.feature.home.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.koniukhov.cinecirclex.core.common.MediaType
import com.koniukhov.cinecirclex.core.common.model.MediaListType
import com.koniukhov.cinecirclex.core.common.navigation.NavArgs.ARG_GENRE_ID
import com.koniukhov.cinecirclex.core.common.navigation.NavArgs.ARG_TITLE
import com.koniukhov.cinecirclex.core.common.navigation.NavArgs.ARG_TYPE
import com.koniukhov.cinecirclex.core.common.navigation.navigateToMovieDetails
import com.koniukhov.cinecirclex.core.common.navigation.navigateToTvSeriesDetails
import com.koniukhov.cinecirclex.core.design.R
import com.koniukhov.cinecirclex.core.ui.adapter.PagingMediaAdapter
import com.koniukhov.cinecirclex.core.ui.base.BaseFragment
import com.koniukhov.cinecirclex.core.ui.utils.GridSpacingItemDecoration
import com.koniukhov.cinecirclex.feature.home.databinding.FragmentMediaListBinding
import com.koniukhov.cinecirclex.feature.home.ui.viewmodel.MediaListViewModel
import com.koniukhov.cinecirclex.feature.home.R as homeR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MediaListFragment : BaseFragment<FragmentMediaListBinding, MediaListViewModel>() {

    override val viewModel: MediaListViewModel by viewModels()

    private var type: MediaListType? = null
    private var title: String? = null
    private var genreId: Int? = null
    private lateinit var adapter: PagingMediaAdapter
    private var isErrorDialogShowing = false

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaListBinding {
        return FragmentMediaListBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupArgs()
        setupTitle()
        setupNavigationClickListener()
        setupRecyclerView()
        loadData()
    }

    override fun observeViewModel() {
        launchWhenStarted {
            observeUiState()
        }
    }

    private suspend fun observeUiState() {
        viewModel.uiState.collectLatest { state ->
            if (state.error != null) {
                showErrorDialog()
            } else {
                state.mediaFlow?.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    private fun setupAdapterLoadStateListener() {
        adapter.addLoadStateListener { loadState ->
            val isLoading = loadState.refresh is LoadState.Loading
            val isError = loadState.refresh is LoadState.Error
            val isEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0

            binding.loadingStateLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.mediaRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE

            if (isError || isEmpty) {
                showErrorDialog()
            }
        }
    }

    private fun showErrorDialog() {
        if (isErrorDialogShowing) return

        isErrorDialogShowing = true
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(homeR.string.network_error_title)
            .setMessage(homeR.string.network_error_message)
            .setCancelable(false)
            .setNegativeButton(homeR.string.back) { _, _ ->
                isErrorDialogShowing = false
                findNavController().popBackStack()
            }
            .setPositiveButton(homeR.string.retry) { _, _ ->
                isErrorDialogShowing = false
                loadData()
            }
            .setOnDismissListener {
                isErrorDialogShowing = false
            }
            .show()
    }

    private fun setupNavigationClickListener() {
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = PagingMediaAdapter{ mediaId, mediaType ->
            if (mediaType == MediaType.MOVIE){
                findNavController().navigateToMovieDetails(mediaId)
            }else if (mediaType == MediaType.TV_SERIES){
                findNavController().navigateToTvSeriesDetails(mediaId)
            }
        }
        binding.mediaRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.mediaRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(2, spacing, true)
        )
        binding.mediaRecyclerView.adapter = adapter
        setupAdapterLoadStateListener()
    }

    private fun setupArgs() {
        val args = requireArguments()
        val typeString = args.getString(ARG_TYPE)
        type = typeString?.let { MediaListType.valueOf(it) }
        title = args.getString(ARG_TITLE)
        genreId = args.getInt(ARG_GENRE_ID)
    }

    private fun setupTitle() {
        binding.topAppBar.title = title
    }


    private fun loadData() {
        val currentType = type
        if (currentType != null) {
            viewModel.loadMedia(currentType, genreId)
        }
    }

    override fun onDestroyView() {
        binding.mediaRecyclerView.adapter = null
        super.onDestroyView()
    }
}