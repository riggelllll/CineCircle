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
import com.koniukhov.cinecircle.core.common.model.MediaListType
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.mediaListUri
import com.koniukhov.cinecircle.feature.home.ui.viewmodel.HomeViewModel
import com.koniukhov.cinecircle.feature.home.R
import com.koniukhov.cinecircle.feature.home.ui.state.TvSeriesUiState
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
        setupSeeAllClickListeners()

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
        binding.genreRecyclerView.adapter = GenreUiAdapter { id, name ->
            val encodedName = Uri.encode(name)
            val request = NavDeepLinkRequest.Builder
                .fromUri(mediaListUri(MediaListType.TV_SERIES_BY_GENRE, encodedName, id))
                .build()
            findNavController().navigate(request)
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
        (binding.airingTodayRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.airingTodayTvSeries)
        (binding.onAirRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.onTheAirTvSeries)
        (binding.trendingRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.trendingTvSeries)
        (binding.popularRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.popularTvSeries)
        (binding.topRatedRecyclerView.adapter as? TvSeriesAdapter)?.setData(ui.topRatedTvSeries)
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
                .fromUri(mediaListUri(type, encodedTitle, -1))
                .build()
            findNavController().navigate(request)
        }
    }

    companion object{
        const val RECYCLER_SKELETON_ITEM_COUNT = 5
    }
}