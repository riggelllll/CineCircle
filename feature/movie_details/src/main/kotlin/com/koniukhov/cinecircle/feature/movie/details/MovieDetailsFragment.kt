package com.koniukhov.cinecircle.feature.movie.details

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil3.load
import coil3.request.placeholder
import com.google.android.material.chip.Chip
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.ARG_MOVIE_ID
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Image
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.movie.details.adapter.MovieImagesAdapter
import com.koniukhov.cinecircle.feature.movie.details.adapter.MovieTrailersAdapter
import com.koniukhov.cinecircle.feature.movie.details.dialog.FullscreenImageDialog
import com.koniukhov.cinecircle.feature.movie.details.dialog.FullscreenVideoDialog
import com.koniukhov.cinecircle.feature.movie.details.utils.MovieDetailsUtils
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
    private lateinit var trailersAdapter: MovieTrailersAdapter
    private lateinit var imagesAdapter: MovieImagesAdapter
    private var isFullscreen = false
    private var currentExitFullscreenFunction: (() -> Unit)? = null
    private var originalOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    private lateinit var windowInsetsController: WindowInsetsControllerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArgs()
        viewModel.loadMovieDetails()
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

        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        windowInsetsController = WindowCompat.getInsetsController(requireActivity().window, view)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setupToolbar()
        setupSectionHeaders()
        setupRecyclerViews()
        observeUiState()
    }

    private fun setupRecyclerViews() {
        trailersAdapter = MovieTrailersAdapter(
            lifecycle = lifecycle,
            onFullscreenEnter = { fullscreenView, exitFullscreen ->
                handleEnterFullscreen(fullscreenView, exitFullscreen)
            },
            onFullscreenExit = {
                handleExitFullscreen()
            }
        )
        binding.recyclerTrailers.adapter = trailersAdapter

        imagesAdapter = MovieImagesAdapter { imagePath ->
            showFullscreenImage(imagePath)
        }
        binding.recyclerImages.adapter = imagesAdapter
    }

    private fun handleEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
        val dialog = FullscreenVideoDialog.newInstance(fullscreenView, exitFullscreen)
        dialog.show(parentFragmentManager, FULLSCREEN_VIDEO_DIALOG_TAG)

        isFullscreen = true
        currentExitFullscreenFunction = exitFullscreen
    }

    private fun handleExitFullscreen() {
        isFullscreen = false
        currentExitFullscreenFunction = null
    }

    private fun showFullscreenImage(imagePath: String) {
        val dialog = FullscreenImageDialog.newInstance(imagePath)
        dialog.show(parentFragmentManager, FULLSCREEN_IMAGE_DIALOG_TAG)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                if (!uiState.isLoading && uiState.error  == null){
                    uiState.movieDetails?.let { movieDetails ->
                        binding.apply {
                            imgBackdrop.load(IMAGE_URL_TEMPLATE.format(movieDetails.backdropPath)){
                                placeholder(com.koniukhov.cinecircle.core.design.R.drawable.placeholder_image)
                            }
                            movieTitle.text = movieDetails.title
                            rating.text = MovieDetailsUtils.formatRating(movieDetails.voteAverage)
                            duration.text = MovieDetailsUtils.formatRuntime(
                                runtime = movieDetails.runtime,
                                hoursLabel = getString(R.string.hours_short),
                                minutesLabel = getString(R.string.minutes_short)
                            )
                            age.text = MovieDetailsUtils.getAgeRating(movieDetails, uiState.releaseDates, viewModel.countryCode)
                            country.text = MovieDetailsUtils.getCountryCode(movieDetails)
                            year.text = MovieDetailsUtils.formatReleaseYear(movieDetails.releaseDate)
                            plotDescription.text = movieDetails.overview
                        }

                        setupGenres(movieDetails.genres)
                    }

                    uiState.videos?.let { movieVideos ->
                        trailersAdapter.setTrailers(movieVideos.results)
                    }

                    uiState.images?.let { mediaImages ->
                        val allImages = mutableListOf<Image>()
                        allImages.addAll(mediaImages.backdrops)
                        allImages.addAll(mediaImages.posters)
                        imagesAdapter.setImages(allImages)
                    }
                }else{
                    Timber.d(uiState.error)
                }
            }
        }
    }

    private fun setupGenres(genres: List<Genre>) {
        binding.chipGroupGenres.removeAllViews()

        genres.forEach { genre ->
            val chip = Chip(requireContext())
            chip.text = genre.name
            chip.isClickable = false
            chip.isCheckable = false
            binding.chipGroupGenres.addView(chip)
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
        currentExitFullscreenFunction?.invoke()
        super.onDestroyView()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (isFullscreen) {
            when (newConfig.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    binding.appBarLayout.visibility = View.GONE
                }
                Configuration.ORIENTATION_PORTRAIT -> {
                    if (!isFullscreen) {
                        binding.appBarLayout.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            binding.appBarLayout.visibility = View.VISIBLE
        }
    }

    companion object{
        private const val FULLSCREEN_IMAGE_DIALOG_TAG = "FullscreenImageDialog"
        private const val FULLSCREEN_VIDEO_DIALOG_TAG = "FullscreenVideoDialog"
    }
}