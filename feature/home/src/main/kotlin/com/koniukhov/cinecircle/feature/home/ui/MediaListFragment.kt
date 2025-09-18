package com.koniukhov.cinecircle.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.koniukhov.cinecircle.core.common.Constants.MediaType
import com.koniukhov.cinecircle.core.common.model.MediaListType
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.ARG_GENRE_ID
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.ARG_TITLE
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.ARG_TYPE
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.movieDetailsUri
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.tvSeriesDetailsUri
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.feature.home.ui.viewmodel.MediaListViewModel
import com.koniukhov.cinecircle.core.ui.adapter.PagingMediaAdapter
import com.koniukhov.cinecircle.feature.home.databinding.FragmentMediaListBinding
import com.koniukhov.cinecircle.core.ui.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaListFragment : Fragment() {
    private var _binding: FragmentMediaListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MediaListViewModel by viewModels()
    private var type: MediaListType? = null
    private var title: String? = null
    private var genreId: Int? = null
    private lateinit var adapter: PagingMediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArgs()
        setupTitle()
        setupNavigationClickListener()
        setupRecyclerView()
        observeUiState()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNavigationClickListener() {
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = PagingMediaAdapter{ mediaId, mediaType ->
            if (mediaType == MediaType.MOVIE){
                navigateToMovieDetails(mediaId)
            }else if (mediaType == MediaType.TV_SERIES){
                navigateToTvSeriesDetails(mediaId)
            }
        }
        binding.mediaRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.mediaRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(2, spacing, true)
        )
        binding.mediaRecyclerView.adapter = adapter
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

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                if (!state.isLoading && state.error == null) {
                    state.mediaFlow?.collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }
            }
        }
    }

    private fun loadData() {
        val currentType = type
        if (currentType != null) {
            viewModel.loadMedia(currentType, genreId)
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val request = NavDeepLinkRequest.Builder
            .fromUri(movieDetailsUri(movieId))
            .build()
        findNavController().navigate(request)
    }

    private fun navigateToTvSeriesDetails(tvSeriesId: Int) {
        val request = NavDeepLinkRequest.Builder
            .fromUri(tvSeriesDetailsUri(tvSeriesId))
            .build()
        findNavController().navigate(request)
    }
}