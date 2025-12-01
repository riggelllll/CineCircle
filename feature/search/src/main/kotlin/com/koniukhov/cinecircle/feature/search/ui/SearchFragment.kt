package com.koniukhov.cinecircle.feature.search.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.common.navigation.navigateToMovieDetails
import com.koniukhov.cinecircle.core.common.navigation.navigateToTvSeriesDetails
import com.koniukhov.cinecircle.core.ui.adapter.PagingMediaAdapter
import com.koniukhov.cinecircle.core.ui.base.BaseFragment
import com.koniukhov.cinecircle.core.ui.utils.GridSpacingItemDecoration
import com.koniukhov.cinecircle.feature.search.databinding.FragmentSearchBinding
import com.koniukhov.cinecircle.feature.search.ui.FiltersDialogFragment.Companion.TAG
import com.koniukhov.cinecircle.feature.search.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import com.koniukhov.cinecircle.core.design.R as designR

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by activityViewModels()
    private lateinit var searchAdapter: PagingMediaAdapter
    private lateinit var filterAdapter: PagingMediaAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupSearchRecyclerView()
        setupFilterRecyclerView()
        setupSearch()
        setupFiltersDialog()
    }

    override fun observeViewModel() {
        launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observeSearchPagingData() }
                launch { observeFilterPagingData() }
            }
        }
    }

    private fun setupSearchRecyclerView() {
        searchAdapter = PagingMediaAdapter { mediaId, mediaType ->
            if (mediaType == MediaType.MOVIE) {
                findNavController().navigateToMovieDetails(mediaId)
            } else if (mediaType == MediaType.TV_SERIES) {
                findNavController().navigateToTvSeriesDetails(mediaId)
            }
        }

        searchAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(designR.dimen.grid_spacing)
        binding.searchRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(2, spacing, true)
        )
        binding.searchRecyclerView.adapter = searchAdapter

        searchAdapter.addLoadStateListener { loadStates ->
            if (loadStates.refresh is LoadState.NotLoading) {
                binding.searchRecyclerView.scrollToPosition(0)
            }
        }
    }

    private fun setupFilterRecyclerView() {
        filterAdapter = PagingMediaAdapter { mediaId, mediaType ->
            if (mediaType == MediaType.MOVIE) {
                findNavController().navigateToMovieDetails(mediaId)
            } else if (mediaType == MediaType.TV_SERIES) {
                findNavController().navigateToTvSeriesDetails(mediaId)
            }
        }

        binding.filtersRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(designR.dimen.grid_spacing)
        binding.filtersRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(2, spacing, true)
        )
        binding.filtersRecyclerView.adapter = filterAdapter

        filterAdapter.addLoadStateListener { loadStates ->
            updateEmptyState(loadStates)
        }
    }

    private fun setupSearch() {
        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                viewModel.onSearchQueryChanged(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchView.editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.clearFocus()
                hideKeyboard(v)
                true
            } else {
                false
            }
        }
    }

    private fun setupFiltersDialog() {
        binding.filterButton.setOnClickListener {
            FiltersDialogFragment().show(parentFragmentManager, TAG)
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private suspend fun observeSearchPagingData() {
        viewModel.searchPagingDataFlow.collectLatest { pagingData ->
            searchAdapter.submitData(pagingData)
        }
    }

    private suspend fun observeFilterPagingData() = coroutineScope {
        launch {
            viewModel.moviesFilterPagingDataFlow.collect { pagingData ->
                Timber.d("Movies filter data received")
                filterAdapter.submitData(pagingData)
            }
        }
        launch {
            viewModel.tvSeriesFilterPagingDataFlow.collect { pagingData ->
                Timber.d("TV Series filter data received")
                filterAdapter.submitData(pagingData)
            }
        }
    }

    private fun updateEmptyState(loadState: CombinedLoadStates) {
        val isLoading = loadState.refresh is LoadState.Loading
        val isError = loadState.refresh is LoadState.Error ||
                loadState.append is LoadState.Error ||
                loadState.prepend is LoadState.Error
        val endReached = (loadState.append as? LoadState.NotLoading)?.endOfPaginationReached == true
        val isEmpty = !isLoading && !isError && endReached && filterAdapter.itemCount == 0

        binding.emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}