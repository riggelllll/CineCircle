package com.koniukhov.cinecircle.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.feature.home.adapter.MoviesAdapter
import com.koniukhov.cinecircle.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.loadMoviesForAllCategories()
            viewModel.uiState.collect {
                initNowPlayingRecyclerView(it.nowPlayingMovies)
                initPopularRecyclerView(it.popularMovies)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initNowPlayingRecyclerView(movies: List<Movie>) {
        binding.nowPlayingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.nowPlayingRecyclerView.adapter = MoviesAdapter(movies){
        }

    }

    private fun initPopularRecyclerView(movies: List<Movie>) {
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.popularRecyclerView.adapter = MoviesAdapter(movies){
        }

    }
}