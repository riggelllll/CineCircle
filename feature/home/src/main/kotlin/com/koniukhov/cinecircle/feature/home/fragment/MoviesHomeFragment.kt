package com.koniukhov.cinecircle.feature.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.koniukhov.cinecircle.feature.home.HomeViewModel
import com.koniukhov.cinecircle.feature.home.MoviesUiState
import com.koniukhov.cinecircle.feature.home.R
import com.koniukhov.cinecircle.feature.home.adapter.GenreUiAdapter
import com.koniukhov.cinecircle.feature.home.adapter.MoviesAdapter
import com.koniukhov.cinecircle.feature.home.databinding.FragmentMoviesHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.trendingRecyclerView.adapter = MoviesAdapter(){
        }
    }
    private fun setupNowPlayingRecyclerView() {
        binding.nowPlayingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.nowPlayingRecyclerView.adapter = MoviesAdapter(){
        }

    }
    private fun setupPopularRecyclerView() {
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.popularRecyclerView.adapter = MoviesAdapter(){
        }

    }
    private fun setupTopRatedRecyclerView() {
        binding.topRatedRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.topRatedRecyclerView.adapter = MoviesAdapter(){
        }
    }
    private fun setupUpcomingRecyclerView() {
        binding.upcomingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.upcomingRecyclerView.adapter = MoviesAdapter(){
        }
    }
    private fun setupMoviesGenreRecyclerView() {
        binding.genreRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.genreRecyclerView.adapter = GenreUiAdapter(){
        }
    }
    private fun setupAllRecyclerSkeletons() {
        trendingSkeleton = binding.trendingRecyclerView.applySkeleton(R.layout.item_home_movie, RECYCLER_SKELETON_ITEM_COUNT)
        mowPlayingSkeleton = binding.nowPlayingRecyclerView.applySkeleton(R.layout.item_home_movie, RECYCLER_SKELETON_ITEM_COUNT)
        popularSkeleton = binding.popularRecyclerView.applySkeleton(R.layout.item_home_movie, RECYCLER_SKELETON_ITEM_COUNT)
        topRatedSkeleton = binding.topRatedRecyclerView.applySkeleton(R.layout.item_home_movie, RECYCLER_SKELETON_ITEM_COUNT)
        upcomingSkeleton = binding.upcomingRecyclerView.applySkeleton(R.layout.item_home_movie, RECYCLER_SKELETON_ITEM_COUNT)
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
        (binding.trendingRecyclerView.adapter as? MoviesAdapter)?.setData(ui.trendingMovies, ui.genreUiMovies)
        (binding.nowPlayingRecyclerView.adapter as? MoviesAdapter)?.setData(ui.nowPlayingMovies, ui.genreUiMovies)
        (binding.popularRecyclerView.adapter as? MoviesAdapter)?.setData(ui.popularMovies, ui.genreUiMovies)
        (binding.topRatedRecyclerView.adapter as? MoviesAdapter)?.setData(ui.topRatedMovies, ui.genreUiMovies)
        (binding.upcomingRecyclerView.adapter as? MoviesAdapter)?.setData(ui.upcomingMovies, ui.genreUiMovies)
        (binding.genreRecyclerView.adapter as? GenreUiAdapter)?.setData(ui.genreUiMovies)
    }

    companion object{
        const val RECYCLER_SKELETON_ITEM_COUNT = 5
    }
}