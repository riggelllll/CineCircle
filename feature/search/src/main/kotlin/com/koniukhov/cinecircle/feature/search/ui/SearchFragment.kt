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
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.search.SearchView
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.common.navigation.navigateToMovieDetails
import com.koniukhov.cinecircle.core.common.navigation.navigateToTvSeriesDetails
import com.koniukhov.cinecircle.core.domain.NetworkStatusProvider
import com.koniukhov.cinecircle.core.ui.adapter.PagingMediaAdapter
import com.koniukhov.cinecircle.core.ui.base.BaseFragment
import com.koniukhov.cinecircle.core.ui.utils.GridSpacingItemDecoration
import com.koniukhov.cinecircle.feature.search.R
import com.koniukhov.cinecircle.feature.search.databinding.FragmentSearchBinding
import com.koniukhov.cinecircle.feature.search.ui.FiltersDialogFragment.Companion.TAG
import com.koniukhov.cinecircle.feature.search.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.koniukhov.cinecircle.core.design.R as designR

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by activityViewModels()
    private lateinit var searchAdapter: PagingMediaAdapter
    private lateinit var filterAdapter: PagingMediaAdapter
    private var searchViewTransitionListener: SearchView.TransitionListener? = null
    private var isNetworkDialogShowing = false

    @Inject
    lateinit var networkStatusProvider: NetworkStatusProvider

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val query = s.toString().trim()
            viewModel.onSearchQueryChanged(query)
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        checkNetworkAndSetupUI()
    }

    private fun checkNetworkAndSetupUI() {
        if (!networkStatusProvider.isNetworkAvailable()) {
            showNoInternetState()
            showNoInternetDialog()
        } else {
            hideNoInternetState()
            setupSearchRecyclerView()
            setupFilterRecyclerView()
            setupSearch()
            setupFiltersDialog()
        }
    }

    private fun showNoInternetState() {
        binding.searchBar.visibility = View.GONE
        binding.filterButton.visibility = View.GONE
        binding.filtersRecyclerView.visibility = View.GONE
        binding.emptyView.visibility = View.GONE
        binding.noInternetView.visibility = View.VISIBLE
    }

    private fun hideNoInternetState() {
        binding.searchBar.visibility = View.VISIBLE
        binding.filterButton.visibility = View.VISIBLE
        binding.noInternetView.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
    }

    private fun showNoInternetDialog() {
        if (isNetworkDialogShowing) return

        isNetworkDialogShowing = true
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.no_internet_connection_title)
            .setMessage(R.string.no_internet_message)
            .setCancelable(false)
            .setPositiveButton(R.string.close) { dialog, _ ->
                isNetworkDialogShowing = false
                dialog.dismiss()
            }
            .setOnDismissListener {
                isNetworkDialogShowing = false
            }
            .show()
    }

    override fun observeViewModel() {
        if (!networkStatusProvider.isNetworkAvailable()) return

        launchWhenStarted {
            launch { observeSearchPagingData() }
            launch { observeFilterPagingData() }
        }
    }

    private fun setupSearchRecyclerView() {
        searchAdapter = PagingMediaAdapter { mediaId, mediaType ->
            if (!isAdded) return@PagingMediaAdapter

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
    }

    private fun setupFilterRecyclerView() {
        filterAdapter = PagingMediaAdapter { mediaId, mediaType ->
            if (!isAdded) return@PagingMediaAdapter

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
        binding.searchView.editText.addTextChangedListener(searchTextWatcher)

        binding.searchView.editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.clearFocus()
                hideKeyboard(v)
                true
            } else {
                false
            }
        }

        searchViewTransitionListener = SearchView.TransitionListener { _, _, newState ->
            if (newState == SearchView.TransitionState.HIDDEN) {
                binding.appBarLayout.setExpanded(true, true)
            }
        }
        binding.searchView.addTransitionListener(searchViewTransitionListener!!)
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
        val itemCount = filterAdapter.itemCount

        val isEmpty = !isLoading && !isError && itemCount == 0

        if (isEmpty) {
            binding.emptyView.visibility = View.VISIBLE
            binding.filtersRecyclerView.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.filtersRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        searchViewTransitionListener?.let {
            binding.searchView.removeTransitionListener(it)
            searchViewTransitionListener = null
        }

        removeTextWatcher()

        binding.searchRecyclerView.adapter = null
        binding.filtersRecyclerView.adapter = null

        try {
            binding.searchView.editText.let { editText ->
                editText.clearFocus()
                hideKeyboard(editText)
            }
        } catch (e: Exception) {
            Timber.d("Error clearing focus from SearchView editText: ${e.message}")
        }

        try {
            binding.searchView.let {
                it.hide()
                it.clearFocusAndHideKeyboard()
            }
        } catch (e: Exception) {
            Timber.d("Error hiding SearchView: ${e.message}")
        }

        clearSearchViewAccessibilityMap(binding.searchView)


        super.onDestroyView()
    }

    private fun clearSearchViewAccessibilityMap(searchView: SearchView) {
        try {
            val field = searchView.javaClass.getDeclaredField("childImportantForAccessibilityMap")
            field.isAccessible = true
            val map = field.get(searchView) as? HashMap<*, *>
            map?.clear()
        } catch (e: NoSuchFieldException) {
            Timber.e(e, "Field 'childImportantForAccessibilityMap' not found in SearchView class!")
        } catch (e: Exception) {
            Timber.e(e, "Failed to clear SearchView accessibility map via reflection")
        }
    }

    private fun removeTextWatcher() {
        binding.searchView.editText.removeTextChangedListener(searchTextWatcher)
    }
}