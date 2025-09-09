package com.koniukhov.cinecircle.feature.home.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID
import com.koniukhov.cinecircle.core.common.model.MediaListType
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.mediaListUri
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.movieDetailsUri
import com.koniukhov.cinecircle.feature.home.ui.viewmodel.HomeViewModel
import com.koniukhov.cinecircle.feature.home.ui.state.MoviesUiState
import com.koniukhov.cinecircle.feature.home.R
import com.koniukhov.cinecircle.feature.home.adapter.GenreUiAdapter
import com.koniukhov.cinecircle.feature.home.adapter.MoviesAdapter
import com.koniukhov.cinecircle.feature.home.databinding.FragmentMoviesHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesHomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentMoviesHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var trendingSkeleton: Skeleton
    private lateinit var mowPlayingSkeleton: Skeleton
    private lateinit var popularSkeleton: Skeleton
    private lateinit var topRatedSkeleton: Skeleton
    private lateinit var upcomingSkeleton: Skeleton
    private lateinit var genreSkeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupAllRecyclerViews()
        setupAllRecyclerSkeletons()
        showAllSkeletons()
        setupSeeAllClickListeners()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMoviesState()
    }

    private fun observeMoviesState() {
        lifecycleScope.launch {
            viewModel.loadMoviesForAllCategories()
            viewModel.moviesUiState.collect {
                if (!it.isLoading && it.error == null){
                    hideAllSkeletons()
                    setDataToRecyclers(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.trendingRecyclerView.adapter = MoviesAdapter{
            navigateToMovieDetails(it)
        }
    }
    private fun setupNowPlayingRecyclerView() {
        binding.nowPlayingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.nowPlayingRecyclerView.adapter = MoviesAdapter {
            navigateToMovieDetails(it)
        }

    }
    private fun setupPopularRecyclerView() {
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.popularRecyclerView.adapter = MoviesAdapter {
            navigateToMovieDetails(it)
        }

    }
    private fun setupTopRatedRecyclerView() {
        binding.topRatedRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.topRatedRecyclerView.adapter = MoviesAdapter {
            navigateToMovieDetails(it)
        }
    }
    private fun setupUpcomingRecyclerView() {
        binding.upcomingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.upcomingRecyclerView.adapter = MoviesAdapter {
            navigateToMovieDetails(it)
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
        trendingSkeleton.showSkeleton()
        mowPlayingSkeleton.showSkeleton()
        popularSkeleton.showSkeleton()
        topRatedSkeleton.showSkeleton()
        upcomingSkeleton.showSkeleton()
        genreSkeleton.showSkeleton()
    }
    private fun hideAllSkeletons() {
        trendingSkeleton.showOriginal()
        mowPlayingSkeleton.showOriginal()
        popularSkeleton.showOriginal()
        topRatedSkeleton.showOriginal()
        upcomingSkeleton.showOriginal()
        genreSkeleton.showOriginal()
    }
    private fun setDataToRecyclers(ui: MoviesUiState){
        (binding.trendingRecyclerView.adapter as? MoviesAdapter)?.setData(ui.trendingMovies)
        (binding.nowPlayingRecyclerView.adapter as? MoviesAdapter)?.setData(ui.nowPlayingMovies)
        (binding.popularRecyclerView.adapter as? MoviesAdapter)?.setData(ui.popularMovies)
        (binding.topRatedRecyclerView.adapter as? MoviesAdapter)?.setData(ui.topRatedMovies)
        (binding.upcomingRecyclerView.adapter as? MoviesAdapter)?.setData(ui.upcomingMovies)
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

    private fun navigateToMovieDetails(movieId: Int) {
        val request = NavDeepLinkRequest.Builder
            .fromUri(movieDetailsUri(movieId))
            .build()
        findNavController().navigate(request)
    }

    companion object{
        const val RECYCLER_SKELETON_ITEM_COUNT = 5
    }
}