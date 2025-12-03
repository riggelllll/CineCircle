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
import com.koniukhov.cinecircle.core.common.navigation.navigateToMovieDetails
import com.koniukhov.cinecircle.core.ui.adapter.MediaAdapter
import com.koniukhov.cinecircle.core.ui.base.BaseFragment
import com.koniukhov.cinecircle.feature.home.R
import com.koniukhov.cinecircle.feature.home.adapter.GenreUiAdapter
import com.koniukhov.cinecircle.feature.home.databinding.FragmentMoviesHomeBinding
import com.koniukhov.cinecircle.feature.home.ui.state.MoviesUiState
import com.koniukhov.cinecircle.feature.home.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MoviesHomeFragment : BaseFragment<FragmentMoviesHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    private var trendingSkeleton: Skeleton? = null
    private var mowPlayingSkeleton: Skeleton? = null
    private var popularSkeleton: Skeleton? = null
    private var topRatedSkeleton: Skeleton? = null
    private var upcomingSkeleton: Skeleton? = null
    private var genreSkeleton: Skeleton? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoviesHomeBinding {
        return FragmentMoviesHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupAllRecyclerViews()
        setupAllRecyclerSkeletons()
        showAllSkeletons()
        setupSeeAllClickListeners()
    }

    override fun observeViewModel() {
        viewModel.loadMoviesForAllCategories()

        launchWhenStarted {
            handleMoviesUiState()
        }
    }

    private suspend fun handleMoviesUiState() {
        viewModel.moviesUiState.collectLatest { uiState ->
            if (!uiState.isLoading && uiState.error == null) {
                if (areAllMovieListsNotEmpty(uiState)) {
                    hideAllSkeletons()
                }
                setDataToRecyclers(uiState)
            }
        }
    }

    private fun setupAllRecyclerViews() {
        setupTrendingRecyclerView()
        setupNowPlayingRecyclerView()
        setupPopularRecyclerView()
        setupTopRatedRecyclerView()
        setupUpcomingRecyclerView()
        setupMoviesGenreRecyclerView()
    }
    private fun setupTrendingRecyclerView() {
        binding.trendingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.trendingRecyclerView.adapter = MediaAdapter { id, _ ->
            findNavController().navigateToMovieDetails(id)
        }
    }
    private fun setupNowPlayingRecyclerView() {
        binding.nowPlayingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.nowPlayingRecyclerView.adapter = MediaAdapter { id, _ ->
            findNavController().navigateToMovieDetails(id)
        }

    }
    private fun setupPopularRecyclerView() {
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.popularRecyclerView.adapter = MediaAdapter { id, _ ->
            findNavController().navigateToMovieDetails(id)
        }

    }
    private fun setupTopRatedRecyclerView() {
        binding.topRatedRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.topRatedRecyclerView.adapter = MediaAdapter { id, _ ->
            findNavController().navigateToMovieDetails(id)
        }
    }
    private fun setupUpcomingRecyclerView() {
        binding.upcomingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.upcomingRecyclerView.adapter = MediaAdapter { id, _ ->
            findNavController().navigateToMovieDetails(id)
        }
    }
    private fun setupMoviesGenreRecyclerView() {
        binding.genreRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.genreRecyclerView.adapter = GenreUiAdapter { id, name ->
            val encodedName = Uri.encode(name)
            val request = NavDeepLinkRequest.Builder
                .fromUri(mediaListUri(MediaListType.MOVIES_BY_GENRE, encodedName, id))
                .build()
            findNavController().navigate(request)
        }
    }
    private fun setupAllRecyclerSkeletons() {
        trendingSkeleton = binding.trendingRecyclerView.applySkeleton(R.layout.item_home_genre_ui, RECYCLER_SKELETON_ITEM_COUNT)
        mowPlayingSkeleton = binding.nowPlayingRecyclerView.applySkeleton(R.layout.item_home_genre_ui, RECYCLER_SKELETON_ITEM_COUNT)
        popularSkeleton = binding.popularRecyclerView.applySkeleton(R.layout.item_home_genre_ui, RECYCLER_SKELETON_ITEM_COUNT)
        topRatedSkeleton = binding.topRatedRecyclerView.applySkeleton(R.layout.item_home_genre_ui, RECYCLER_SKELETON_ITEM_COUNT)
        upcomingSkeleton = binding.upcomingRecyclerView.applySkeleton(R.layout.item_home_genre_ui, RECYCLER_SKELETON_ITEM_COUNT)
        genreSkeleton = binding.genreRecyclerView.applySkeleton(R.layout.item_home_genre_ui, RECYCLER_SKELETON_ITEM_COUNT)
    }
    private fun showAllSkeletons() {
        trendingSkeleton?.showSkeleton()
        mowPlayingSkeleton?.showSkeleton()
        popularSkeleton?.showSkeleton()
        topRatedSkeleton?.showSkeleton()
        upcomingSkeleton?.showSkeleton()
        genreSkeleton?.showSkeleton()
    }
    private fun hideAllSkeletons() {
        trendingSkeleton?.showOriginal()
        mowPlayingSkeleton?.showOriginal()
        popularSkeleton?.showOriginal()
        topRatedSkeleton?.showOriginal()
        upcomingSkeleton?.showOriginal()
        genreSkeleton?.showOriginal()
    }
    private fun setDataToRecyclers(ui: MoviesUiState){
        (binding.trendingRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.trendingMovies)
        (binding.nowPlayingRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.nowPlayingMovies)
        (binding.popularRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.popularMovies)
        (binding.topRatedRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.topRatedMovies)
        (binding.upcomingRecyclerView.adapter as? MediaAdapter)?.setMediaItems(ui.upcomingMovies)
        (binding.genreRecyclerView.adapter as? GenreUiAdapter)?.setData(ui.genreUiMovies)
    }

    private fun setupSeeAllClickListeners() {
        setupSeeAllClickListener(
            binding.nowPlayingSeeAll,
            MediaListType.NOW_PLAYING_MOVIES,
            R.string.now_playing_title
        )
        setupSeeAllClickListener(
            binding.trendingSeeAll,
            MediaListType.TRENDING_MOVIES,
            R.string.trending_title
        )
        setupSeeAllClickListener(
            binding.popularSeeAll,
            MediaListType.POPULAR_MOVIES,
            R.string.popular_title
        )
        setupSeeAllClickListener(
            binding.topRatedSeeAll,
            MediaListType.TOP_RATED_MOVIES,
            R.string.top_rated_title
        )
        setupSeeAllClickListener(
            binding.upcomingSeeAll,
            MediaListType.UPCOMING_MOVIES,
            R.string.upcoming_title
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

    private fun areAllMovieListsNotEmpty(state: MoviesUiState): Boolean {
        return state.trendingMovies.isNotEmpty() &&
                state.nowPlayingMovies.isNotEmpty() &&
                state.popularMovies.isNotEmpty() &&
                state.topRatedMovies.isNotEmpty() &&
                state.upcomingMovies.isNotEmpty() &&
                state.genreUiMovies.isNotEmpty()
    }

    override fun onDestroyView() {
        binding.trendingRecyclerView.adapter = null
        binding.nowPlayingRecyclerView.adapter = null
        binding.popularRecyclerView.adapter = null
        binding.topRatedRecyclerView.adapter = null
        binding.upcomingRecyclerView.adapter = null
        binding.genreRecyclerView.adapter = null

        trendingSkeleton = null
        mowPlayingSkeleton = null
        popularSkeleton = null
        topRatedSkeleton = null
        upcomingSkeleton = null
        genreSkeleton = null

        super.onDestroyView()
    }

    companion object{
        const val RECYCLER_SKELETON_ITEM_COUNT = 5
    }
}