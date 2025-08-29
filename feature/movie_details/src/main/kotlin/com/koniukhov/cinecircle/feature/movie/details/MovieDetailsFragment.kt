package com.koniukhov.cinecircle.feature.movie.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.ARG_MOVIE_ID
import com.koniukhov.cinecircle.feature.movie_details.R
import com.koniukhov.cinecircle.feature.movie_details.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArgs()
        viewModel.loadAllInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupSectionHeaders()
        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                if (!uiState.isLoading && uiState.error  == null){
                    Timber.d("Movie details loaded: ${uiState.movieDetails?.title}")
                }
            }
        }
    }

    private fun setupArgs(){
        val args = requireArguments()
        val movieId = args.getInt(ARG_MOVIE_ID, -1)
        viewModel.setMovieId(movieId)
    }

    private fun setupToolbar() = with(binding.topAppBar) {
        setupNavigationClickListener()
        setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_favorite -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun setupNavigationClickListener() {
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupSectionHeaders() = with(binding) {
        sectionImages.sectionTitle.setText(R.string.images_title)
        sectionTrailers.sectionTitle.setText(R.string.trailers_teasers_title)
        sectionCast.sectionTitle.setText(R.string.cast_title)
        sectionCrew.sectionTitle.setText(R.string.crew_title)
        sectionReviews.sectionTitle.setText(R.string.reviews_title)
        sectionRecommendations.sectionTitle.setText(R.string.recommendations_title)
        sectionSimilar.sectionTitle.setText(R.string.similar_title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}