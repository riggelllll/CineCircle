package com.koniukhov.cinecircle.feature.media.details.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import coil3.load
import coil3.request.placeholder
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.chip.Chip
import com.koniukhov.cinecircle.core.common.navigation.NavArgs
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Image
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.model.MovieReview
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints
import com.koniukhov.cinecircle.feature.media.details.ui.viewmodel.MovieDetailsViewModel
import com.koniukhov.cinecircle.feature.media.details.adapter.MovieCastAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MovieCrewAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MovieImagesAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MovieRecommendationsAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MovieReviewsAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MovieTrailersAdapter
import com.koniukhov.cinecircle.feature.media.details.dialog.FullscreenImageDialog
import com.koniukhov.cinecircle.feature.media.details.dialog.FullscreenVideoDialog
import com.koniukhov.cinecircle.feature.media.details.dialog.ReviewDetailBottomSheetDialog
import com.koniukhov.cinecircle.feature.media.details.utils.MovieDetailsUtils
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
    private lateinit var castAdapter: MovieCastAdapter
    private lateinit var crewAdapter: MovieCrewAdapter
    private lateinit var reviewsAdapter: MovieReviewsAdapter
    private lateinit var recommendationsAdapter: MovieRecommendationsAdapter
    private lateinit var similarMoviesAdapter: MovieRecommendationsAdapter
    private var isFullscreen = false
    private var currentExitFullscreenFunction: (() -> Unit)? = null
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private var fullscreenVideoDialog: FullscreenVideoDialog? = null

    private lateinit var containerBackdropSkeleton: Skeleton
    private lateinit var imagesRecyclerSkeleton: Skeleton
    private lateinit var trailersRecyclerSkeleton: Skeleton
    private lateinit var castRecyclerSkeleton: Skeleton
    private lateinit var crewRecyclerSkeleton: Skeleton
    private lateinit var reviewsRecyclerSkeleton: Skeleton
    private lateinit var recommendationsRecyclerSkeleton: Skeleton
    private lateinit var similarRecyclerSkeleton: Skeleton
    private lateinit var plotSkeleton: Skeleton
    private lateinit var aboutSkeleton: Skeleton

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
        setupRecyclerViews()
        setupSkeletons()
        showSkeletons()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWindowInsets(view)
        setupToolbar()
        setupSectionHeaders()
        observeUiState()
    }

    private fun setupWindowInsets(view: View) {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        windowInsetsController = WindowCompat.getInsetsController(requireActivity().window, view)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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

        castAdapter = MovieCastAdapter()
        binding.recyclerCast.adapter = castAdapter

        crewAdapter = MovieCrewAdapter()
        binding.recyclerCrew.adapter = crewAdapter

        reviewsAdapter = MovieReviewsAdapter { review ->
            showReviewDetail(review)
        }
        binding.recyclerReviews.adapter = reviewsAdapter

        recommendationsAdapter = MovieRecommendationsAdapter { movieId ->
            navigateToMovieDetails(movieId)
        }
        binding.recyclerRecommendations.adapter = recommendationsAdapter

        similarMoviesAdapter = MovieRecommendationsAdapter { movieId ->
            navigateToMovieDetails(movieId)
        }
        binding.recyclerSimilar.adapter = similarMoviesAdapter
    }

    private fun showReviewDetail(review: MovieReview) {
        val dialog = ReviewDetailBottomSheetDialog.Companion.newInstance(review)
        dialog.show(parentFragmentManager, REVIEW_DETAIL_DIALOG_TAG)
    }

    private fun handleEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
        val dialog = FullscreenVideoDialog.Companion.newInstance(fullscreenView, exitFullscreen)
        fullscreenVideoDialog = dialog
        dialog.show(parentFragmentManager, FULLSCREEN_VIDEO_DIALOG_TAG)

        isFullscreen = true
        currentExitFullscreenFunction = exitFullscreen
    }

    private fun handleExitFullscreen() {
        isFullscreen = false
        currentExitFullscreenFunction = null
        fullscreenVideoDialog?.dismiss()
        fullscreenVideoDialog = null
    }

    private fun showFullscreenImage(imagePath: String) {
        val dialog = FullscreenImageDialog.Companion.newInstance(imagePath)
        dialog.show(parentFragmentManager, FULLSCREEN_IMAGE_DIALOG_TAG)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                if (!uiState.isLoading && uiState.error  == null){
                    hideSkeletons()
                    uiState.movieDetails?.let { movieDetails ->
                        binding.apply {
                            imgBackdrop.load(TMDBEndpoints.IMAGE_URL_TEMPLATE.format(movieDetails.backdropPath)){
                                placeholder(R.drawable.placeholder_image)
                            }
                            movieTitle.text = movieDetails.title
                            rating.text = MovieDetailsUtils.formatRating(movieDetails.voteAverage)
                            duration.text = MovieDetailsUtils.formatRuntime(
                                runtime = movieDetails.runtime,
                                hoursLabel = getString(com.koniukhov.cinecircle.feature.movie_details.R.string.hours_short),
                                minutesLabel = getString(com.koniukhov.cinecircle.feature.movie_details.R.string.minutes_short)
                            )
                            age.text = MovieDetailsUtils.getAgeRating(movieDetails, uiState.releaseDates, viewModel.countryCode)
                            country.text = MovieDetailsUtils.getCountryCode(movieDetails)
                            year.text = MovieDetailsUtils.formatReleaseYear(movieDetails.releaseDate)
                            plotDescription.text = movieDetails.overview
                        }

                        setupGenres(movieDetails.genres)
                        setupAboutSection(movieDetails)
                    }

                    uiState.videos?.let { movieVideos ->
                        if (movieVideos.results.isEmpty()) {
                            binding.recyclerTrailers.visibility = View.GONE
                            binding.containerNoTrailer.visibility = View.VISIBLE
                        } else {
                            trailersAdapter.setTrailers(movieVideos.results)
                        }
                    }

                    uiState.images?.let { mediaImages ->
                        val allImages = mutableListOf<Image>()
                        allImages.addAll(mediaImages.backdrops)
                        allImages.addAll(mediaImages.posters)

                        if (allImages.isEmpty()){
                            binding.recyclerImages.visibility = View.GONE
                            binding.containerNoImages.visibility = View.VISIBLE
                        } else {
                            imagesAdapter.setImages(allImages)
                        }
                    }

                    uiState.credits?.cast?.let { cast ->
                        castAdapter.setCastMembers(cast)
                    }

                    uiState.credits?.crew?.let { crew ->
                        crewAdapter.setCrewMembers(crew)
                    }

                    reviewsAdapter.setReviews(uiState.reviews)

                    recommendationsAdapter.setMovies(uiState.recommendations)
                    similarMoviesAdapter.setMovies(uiState.similarMovies)

                    if (uiState.reviews.isEmpty()) {
                        binding.recyclerReviews.visibility = View.GONE
                        binding.containerNoReviews.visibility = View.VISIBLE
                    } else {
                        binding.recyclerReviews.visibility = View.VISIBLE
                        binding.containerNoReviews.visibility = View.GONE
                    }
                }else{
                    Timber.Forest.d(uiState.error)
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
        val movieId = args.getInt(NavArgs.ARG_MOVIE_ID, -1)
        viewModel.setMovieId(movieId)
    }

    private fun setupToolbar() = with(binding.topAppBar) {
        setupNavigationClickListener()
        setOnMenuItemClickListener { item ->
            when (item.itemId) {
                com.koniukhov.cinecircle.feature.movie_details.R.id.action_favorite -> {
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
        sectionImages.sectionTitle.setText(com.koniukhov.cinecircle.feature.movie_details.R.string.images_title)
        sectionTrailers.sectionTitle.setText(com.koniukhov.cinecircle.feature.movie_details.R.string.trailers_teasers_title)
        sectionCast.sectionTitle.setText(com.koniukhov.cinecircle.feature.movie_details.R.string.cast_title)
        sectionCrew.sectionTitle.setText(com.koniukhov.cinecircle.feature.movie_details.R.string.crew_title)
        sectionReviews.sectionTitle.setText(com.koniukhov.cinecircle.feature.movie_details.R.string.reviews_title)
        sectionRecommendations.sectionTitle.setText(com.koniukhov.cinecircle.feature.movie_details.R.string.recommendations_title)
        sectionSimilar.sectionTitle.setText(com.koniukhov.cinecircle.feature.movie_details.R.string.similar_title)
    }

    private fun setupAboutSection(movieDetails: MovieDetails) {
        with(binding.sectionAbout) {
            budgetValue.text = if (movieDetails.budget > 0) {
                "$${String.format("%,d", movieDetails.budget)}"
            } else {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            revenueValue.text = if (movieDetails.revenue > 0) {
                "$${String.format("%,d", movieDetails.revenue)}"
            } else {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            originalTitleValue.text = movieDetails.originalTitle.ifEmpty {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            originalLanguageValue.text = movieDetails.originalLanguage.ifEmpty {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            statusValue.text = movieDetails.status.ifEmpty {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            popularityValue.text = String.format("%.1f", movieDetails.popularity)

            productionCountriesValue.text = if (movieDetails.productionCountries.isNotEmpty()) {
                movieDetails.productionCountries.joinToString(", ") { it.name }
            } else {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            spokenLanguagesValue.text = if (movieDetails.spokenLanguages.isNotEmpty()) {
                movieDetails.spokenLanguages.filter { it.name.isNotEmpty() }.joinToString(", ") { it.name }
            } else {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            homepageValue.text = movieDetails.homePage.ifEmpty {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            if (movieDetails.homePage.isNotEmpty()) {
                homepageValue.setOnClickListener {
                    openWebsite(movieDetails.homePage)
                }
                homepageValue.isClickable = true
            } else {
                homepageValue.setOnClickListener(null)
                homepageValue.isClickable = false
            }

            releaseDateValue.text = movieDetails.releaseDate.ifEmpty {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            runtimeValue.text = if (movieDetails.runtime > 0) {
                MovieDetailsUtils.formatRuntime(
                    runtime = movieDetails.runtime,
                    hoursLabel = getString(com.koniukhov.cinecircle.feature.movie_details.R.string.hours_short),
                    minutesLabel = getString(com.koniukhov.cinecircle.feature.movie_details.R.string.minutes_short)
                )
            } else {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            taglineValue.text = movieDetails.tagline.ifEmpty {
                getString(com.koniukhov.cinecircle.feature.movie_details.R.string.not_available)
            }

            voteCountValue.text = String.format("%,d", movieDetails.voteCount)
        }
    }

    private fun openWebsite(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Timber.Forest.e(e, "Failed to open website: $url")
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val request = NavDeepLinkRequest.Builder
            .fromUri(NavArgs.movieDetailsUri(movieId))
            .build()
        findNavController().navigate(request)
    }

    override fun onDestroyView() {
        currentExitFullscreenFunction?.invoke()
        fullscreenVideoDialog?.dismiss()
        fullscreenVideoDialog = null
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

    private fun setupSkeletons() {
        containerBackdropSkeleton = binding.skeletonBackdrop
        plotSkeleton = binding.skeletonPlot
        imagesRecyclerSkeleton = binding.recyclerImages.applySkeleton(com.koniukhov.cinecircle.feature.movie_details.R.layout.item_movie_image, RECYCLER_SKELETON_ITEM_COUNT)
        trailersRecyclerSkeleton = binding.recyclerTrailers.applySkeleton(com.koniukhov.cinecircle.feature.movie_details.R.layout.item_movie_image, RECYCLER_SKELETON_ITEM_COUNT)
        castRecyclerSkeleton = binding.recyclerCast.applySkeleton(com.koniukhov.cinecircle.feature.movie_details.R.layout.item_person, RECYCLER_SKELETON_ITEM_COUNT)
        crewRecyclerSkeleton = binding.recyclerCrew.applySkeleton(com.koniukhov.cinecircle.feature.movie_details.R.layout.item_person, RECYCLER_SKELETON_ITEM_COUNT)
        reviewsRecyclerSkeleton = binding.recyclerReviews.applySkeleton(com.koniukhov.cinecircle.feature.movie_details.R.layout.item_review, RECYCLER_SKELETON_ITEM_COUNT)
        recommendationsRecyclerSkeleton = binding.recyclerRecommendations.applySkeleton(R.layout.item_media, RECYCLER_SKELETON_ITEM_COUNT)
        similarRecyclerSkeleton = binding.recyclerSimilar.applySkeleton(R.layout.item_media, RECYCLER_SKELETON_ITEM_COUNT)
        aboutSkeleton = binding.skeletonAbout
    }

    private fun showSkeletons() {
        containerBackdropSkeleton.showSkeleton()
        plotSkeleton.showSkeleton()
        imagesRecyclerSkeleton.showSkeleton()
        trailersRecyclerSkeleton.showSkeleton()
        castRecyclerSkeleton.showSkeleton()
        crewRecyclerSkeleton.showSkeleton()
        reviewsRecyclerSkeleton.showSkeleton()
        recommendationsRecyclerSkeleton.showSkeleton()
        similarRecyclerSkeleton.showSkeleton()
        aboutSkeleton.showSkeleton()
    }

    private fun hideSkeletons() {
        containerBackdropSkeleton.showOriginal()
        plotSkeleton.showOriginal()
        imagesRecyclerSkeleton.showOriginal()
        trailersRecyclerSkeleton.showOriginal()
        castRecyclerSkeleton.showOriginal()
        crewRecyclerSkeleton.showOriginal()
        reviewsRecyclerSkeleton.showOriginal()
        recommendationsRecyclerSkeleton.showOriginal()
        similarRecyclerSkeleton.showOriginal()
        aboutSkeleton.showOriginal()
    }

    companion object{
        private const val FULLSCREEN_IMAGE_DIALOG_TAG = "FullscreenImageDialog"
        private const val FULLSCREEN_VIDEO_DIALOG_TAG = "FullscreenVideoDialog"
        private const val REVIEW_DETAIL_DIALOG_TAG = "ReviewDetailDialog"
        private const val RECYCLER_SKELETON_ITEM_COUNT = 5
    }
}