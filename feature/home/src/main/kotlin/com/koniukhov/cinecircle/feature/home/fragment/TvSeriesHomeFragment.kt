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
import com.koniukhov.cinecircle.feature.home.R
import com.koniukhov.cinecircle.feature.home.TvSeriesUiState
import com.koniukhov.cinecircle.feature.home.adapter.GenreUiAdapter
import com.koniukhov.cinecircle.feature.home.adapter.TvSeriesAdapter
import com.koniukhov.cinecircle.feature.home.databinding.FragmentTvSeriesHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvSeriesHomeFragment : Fragment() {
    private var _binding: FragmentTvSeriesHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var airingTodaySkeleton: Skeleton
    private lateinit var onAirSkeleton: Skeleton
    private lateinit var trendingSkeleton: Skeleton
    private lateinit var popularSkeleton: Skeleton
    private lateinit var topRatedSkeleton: Skeleton
    private lateinit var genreSkeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvSeriesHomeBinding.inflate(inflater, container, false)

        setupAllRecyclerViews()
        setupAllRecyclerSkeletons()
        showAllSkeletons()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.loadTvSeriesForAllCategories(1)
            viewModel.tvSeriesUiState.collect {
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

    private fun setupAllRecyclerSkeletons() {
        setupAiringTodaySkeleton()
        setupOnAirSkeleton()
        setupTrendingSkeleton()
        setupPopularSkeleton()
        setupTopRatedSkeleton()
        setupGenreSkeleton()
    }

    private fun setupAiringTodaySkeleton() {
        airingTodaySkeleton = binding.airingTodayRecyclerView.applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)

    }

    private fun setupOnAirSkeleton() {
        onAirSkeleton = binding.onAirRecyclerView.applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupTrendingSkeleton() {
        trendingSkeleton = binding.trendingRecyclerView.applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupPopularSkeleton() {
        popularSkeleton = binding.popularRecyclerView.applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupTopRatedSkeleton() {
        topRatedSkeleton = binding.topRatedRecyclerView.applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
    }

    private fun setupGenreSkeleton() {
        genreSkeleton = binding.genreRecyclerView.applySkeleton(R.layout.item_home_genre_ui,RECYCLER_SKELETON_ITEM_COUNT)
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
        binding.airingTodayRecyclerView.adapter = TvSeriesAdapter(){
        }
    }

    private fun setupOnAirRecyclerView() {
        binding.onAirRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.onAirRecyclerView.adapter = TvSeriesAdapter(){
        }
    }

    private fun setupTrendingRecyclerView() {
        binding.trendingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.trendingRecyclerView.adapter = TvSeriesAdapter(){
        }
    }

    private fun setupPopularRecyclerView() {
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.popularRecyclerView.adapter = TvSeriesAdapter(){
        }
    }

    private fun setupTopRatedRecyclerView() {
        binding.topRatedRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.topRatedRecyclerView.adapter = TvSeriesAdapter(){
        }
    }

    private fun setupGenreRecyclerView() {
        binding.genreRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.genreRecyclerView.adapter = GenreUiAdapter(){
        }
    }

    private fun showAllSkeletons() {
        airingTodaySkeleton.showSkeleton()
        onAirSkeleton.showSkeleton()
        trendingSkeleton.showSkeleton()
        popularSkeleton.showSkeleton()
        topRatedSkeleton.showSkeleton()
        genreSkeleton.showSkeleton()
    }

    private fun hideAllSkeletons() {
        airingTodaySkeleton.showOriginal()
        onAirSkeleton.showOriginal()
        trendingSkeleton.showOriginal()
        popularSkeleton.showOriginal()
        topRatedSkeleton.showOriginal()
        genreSkeleton.showOriginal()
    }

    private fun setDataToRecyclers(ui: TvSeriesUiState){
        (binding.airingTodayRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.airingTodayTvSeries, ui.genreUiTvSeries)
        (binding.onAirRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.onTheAirTvSeries, ui.genreUiTvSeries)
        (binding.trendingRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.trendingTvSeries, ui.genreUiTvSeries)
        (binding.popularRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.popularTvSeries, ui.genreUiTvSeries)
        (binding.topRatedRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.topRatedTvSeries, ui.genreUiTvSeries)
        (binding.genreRecyclerView.adapter as? GenreUiAdapter)?.setData(ui.genreUiTvSeries)
    }

    companion object{
        const val RECYCLER_SKELETON_ITEM_COUNT = 5
    }
}