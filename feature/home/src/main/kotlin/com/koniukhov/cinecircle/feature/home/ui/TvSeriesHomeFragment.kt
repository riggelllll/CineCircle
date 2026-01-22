package com.koniukhov.cinecircle.feature.home.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID
import com.koniukhov.cinecircle.core.common.model.MediaListType
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.mediaListUri
import com.koniukhov.cinecircle.core.common.navigation.navigateToTvSeriesDetails
import com.koniukhov.cinecircle.core.ui.adapter.MediaAdapter
import com.koniukhov.cinecircle.core.ui.base.BaseFragment
import com.koniukhov.cinecircle.feature.home.R
import com.koniukhov.cinecircle.feature.home.adapter.GenreUiAdapter
import com.koniukhov.cinecircle.feature.home.databinding.FragmentTvSeriesHomeBinding
import com.koniukhov.cinecircle.feature.home.ui.state.TvSeriesUiState
import com.koniukhov.cinecircle.feature.home.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TvSeriesHomeFragment : BaseFragment<FragmentTvSeriesHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    private var airingTodaySkeleton: Skeleton? = null
    private var onAirSkeleton: Skeleton? = null
    private var trendingSkeleton: Skeleton? = null
    private var popularSkeleton: Skeleton? = null
    private var topRatedSkeleton: Skeleton? = null
    private var genreSkeleton: Skeleton? = null

    private var airingTodayTitleSkeleton: Skeleton? = null
    private var onAirTitleSkeleton: Skeleton? = null
    private var trendingTitleSkeleton: Skeleton? = null
    private var popularTitleSkeleton: Skeleton? = null
    private var topRatedTitleSkeleton: Skeleton? = null
    private var genreTitleSkeleton: Skeleton? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvSeriesHomeBinding {
        return FragmentTvSeriesHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupAllRecyclerViews()
        setupAllRecyclerSkeletons()
        showAllSkeletons()
        setupSeeAllClickListeners()
    }

    override fun observeViewModel() {
        viewModel.loadTvSeriesForAllCategories(1)

        launchWhenStarted {
            observeTvSeriesUiState()
        }
    }

    private suspend fun observeTvSeriesUiState() {
        viewModel.tvSeriesUiState.collectLatest { uiState ->
            if (!uiState.isLoading) {
                if (uiState.error != null || !areAllTvSeriesListsNotEmpty(uiState)) {
                    notifyNetworkError()
                } else {
                    hideAllSkeletons()
                    setDataToRecyclers(uiState)
                }
            }
        }
    }

    private fun notifyNetworkError() {
        (parentFragment as? NetworkErrorListener)?.onNetworkError(HomeFragment.FRAGMENT_TYPE_TV_SERIES)
    }

    fun retryLoading() {
        showAllSkeletons()
        viewModel.loadTvSeriesForAllCategories(forceRefresh = true)
    }

    private fun setupAllRecyclerSkeletons() {
        setupAiringTodaySkeleton()
        setupOnAirSkeleton()
        setupTrendingSkeleton()
        setupPopularSkeleton()
        setupTopRatedSkeleton()
        setupGenreSkeleton()
        setupTitleSkeletons()
    }

    private fun setupAiringTodaySkeleton() {
        airingTodaySkeleton = binding.airingTodayRecyclerView
            .applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)

    }

    private fun setupOnAirSkeleton() {
        onAirSkeleton = binding.onAirRecyclerView
            .applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupTrendingSkeleton() {
        trendingSkeleton = binding.trendingRecyclerView
            .applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupPopularSkeleton() {
        popularSkeleton = binding.popularRecyclerView
            .applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupTopRatedSkeleton() {
        topRatedSkeleton = binding.topRatedRecyclerView
            .applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupGenreSkeleton() {
        genreSkeleton = binding.genreRecyclerView
            .applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupTitleSkeletons() {
        airingTodayTitleSkeleton = binding.airingTodayTitleSkeleton
        onAirTitleSkeleton = binding.onAirTitleSkeleton
        trendingTitleSkeleton = binding.trendingTitleSkeleton
        popularTitleSkeleton = binding.popularTitleSkeleton
        topRatedTitleSkeleton = binding.topRatedTitleSkeleton
        genreTitleSkeleton = binding.genreTitleSkeleton
    }

    private fun setupAllRecyclerViews() {
        setupAiringTodayRecyclerView()
        setupOnAirRecyclerView()
        setupTrendingRecyclerView()
        setupPopularRecyclerView()
        setupTopRatedRecyclerView()
        setupGenreRecyclerView()
    }

    private fun setupAiringTodayRecyclerView() {
        binding.airingTodayRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.airingTodayRecyclerView.adapter = MediaAdapter{ id, _ ->
            findNavController().navigateToTvSeriesDetails(id)
        }
    }

    private fun setupOnAirRecyclerView() {
        binding.onAirRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.onAirRecyclerView.adapter = MediaAdapter{ id, _ ->
            findNavController().navigateToTvSeriesDetails(id)
        }
    }

    private fun setupTrendingRecyclerView() {
        binding.trendingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.trendingRecyclerView.adapter = MediaAdapter{ id, _ ->
            findNavController().navigateToTvSeriesDetails(id)
        }
    }

    private fun setupPopularRecyclerView() {
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.popularRecyclerView.adapter = MediaAdapter{ id, _ ->
            findNavController().navigateToTvSeriesDetails(id)
        }
    }

    private fun setupTopRatedRecyclerView() {
        binding.topRatedRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.topRatedRecyclerView.adapter = MediaAdapter{ id, _ ->
            findNavController().navigateToTvSeriesDetails(id)
        }
    }

    private fun setupGenreRecyclerView() {
        binding.genreRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.genreRecyclerView.adapter = GenreUiAdapter { id, name ->
            val encodedName = Uri.encode(name)
            val request = NavDeepLinkRequest.Builder
                .fromUri(mediaListUri(MediaListType.TV_SERIES_BY_GENRE, encodedName, id))
                .build()
            findNavController().navigate(request)
        }
    }

    private fun showAllSkeletons() {
        airingTodaySkeleton?.showSkeleton()
        onAirSkeleton?.showSkeleton()
        trendingSkeleton?.showSkeleton()
        popularSkeleton?.showSkeleton()
        topRatedSkeleton?.showSkeleton()
        genreSkeleton?.showSkeleton()

        airingTodayTitleSkeleton?.showSkeleton()
        onAirTitleSkeleton?.showSkeleton()
        trendingTitleSkeleton?.showSkeleton()
        popularTitleSkeleton?.showSkeleton()
        topRatedTitleSkeleton?.showSkeleton()
        genreTitleSkeleton?.showSkeleton()
    }

    private fun hideAllSkeletons() {
        airingTodaySkeleton?.showOriginal()
        onAirSkeleton?.showOriginal()
        trendingSkeleton?.showOriginal()
        popularSkeleton?.showOriginal()
        topRatedSkeleton?.showOriginal()
        genreSkeleton?.showOriginal()

        airingTodayTitleSkeleton?.showOriginal()
        onAirTitleSkeleton?.showOriginal()
        trendingTitleSkeleton?.showOriginal()
        popularTitleSkeleton?.showOriginal()
        topRatedTitleSkeleton?.showOriginal()
        genreTitleSkeleton?.showOriginal()
    }

    private fun setDataToRecyclers(ui: TvSeriesUiState){
        (binding.airingTodayRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.airingTodayTvSeries)
        (binding.onAirRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.onTheAirTvSeries)
        (binding.trendingRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.trendingTvSeries)
        (binding.popularRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.popularTvSeries)
        (binding.topRatedRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.topRatedTvSeries)
        (binding.genreRecyclerView.adapter as? GenreUiAdapter)?.setData(ui.genreUiTvSeries)
    }

    private fun setupSeeAllClickListeners() {
        setupSeeAllClickListener(
            binding.airingTodaySeeAll,
            MediaListType.AIRING_TODAY_TV_SERIES,
            R.string.airing_today_title
        )
        setupSeeAllClickListener(
            binding.onAirSeeAll,
            MediaListType.ON_THE_AIR_TV_SERIES,
            R.string.on_the_air_title
        )
        setupSeeAllClickListener(
            binding.trendingSeeAll,
            MediaListType.TRENDING_TV_SERIES,
            R.string.trending_title
        )
        setupSeeAllClickListener(
            binding.popularSeeAll,
            MediaListType.POPULAR_TV_SERIES,
            R.string.popular_title
        )
        setupSeeAllClickListener(
            binding.topRatedSeeAll,
            MediaListType.TOP_RATED_TV_SERIES,
            R.string.top_rated_title
        )
    }

    private fun setupSeeAllClickListener(
        view: View,
        type: MediaListType,
        titleRes: Int
    ) {
        view.setOnClickListener {
            val encodedTitle = Uri.encode(getString(titleRes))
            val request = NavDeepLinkRequest.Builder
                .fromUri(mediaListUri(type, encodedTitle, INVALID_ID))
                .build()
            findNavController().navigate(request)
        }
    }

    private fun areAllTvSeriesListsNotEmpty(state: TvSeriesUiState): Boolean {
        return state.airingTodayTvSeries.isNotEmpty()
                && state.onTheAirTvSeries.isNotEmpty()
                && state.trendingTvSeries.isNotEmpty()
                && state.popularTvSeries.isNotEmpty()
                && state.topRatedTvSeries.isNotEmpty()
                && state.genreUiTvSeries.isNotEmpty()
    }

    override fun onDestroyView() {
        binding.airingTodayRecyclerView.adapter = null
        binding.onAirRecyclerView.adapter = null
        binding.trendingRecyclerView.adapter = null
        binding.popularRecyclerView.adapter = null
        binding.topRatedRecyclerView.adapter = null
        binding.genreRecyclerView.adapter = null

        airingTodaySkeleton = null
        onAirSkeleton = null
        trendingSkeleton = null
        popularSkeleton = null
        topRatedSkeleton = null
        genreSkeleton = null

        airingTodayTitleSkeleton = null
        onAirTitleSkeleton = null
        trendingTitleSkeleton = null
        popularTitleSkeleton = null
        topRatedTitleSkeleton = null
        genreTitleSkeleton = null

        super.onDestroyView()
    }

    companion object{
        const val RECYCLER_SKELETON_ITEM_COUNT = 5
    }
}