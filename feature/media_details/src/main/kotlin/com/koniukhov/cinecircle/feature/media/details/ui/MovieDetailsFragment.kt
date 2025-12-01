package com.koniukhov.cinecircle.feature.media.details.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil3.load
import coil3.request.placeholder
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID
import com.koniukhov.cinecircle.core.common.navigation.NavArgs
import com.koniukhov.cinecircle.core.common.navigation.navigateToMovieDetails
import com.koniukhov.cinecircle.core.common.util.DateUtils.formatDate
import com.koniukhov.cinecircle.core.domain.model.CollectionDetails
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Image
import com.koniukhov.cinecircle.core.domain.model.MediaCredits
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.MovieDetails
import com.koniukhov.cinecircle.core.domain.model.MediaVideos
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints
import com.koniukhov.cinecircle.core.ui.adapter.MediaAdapter
import com.koniukhov.cinecircle.core.ui.utils.openWebsite
import com.koniukhov.cinecircle.feature.media.details.adapter.CollectionMediaAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaCastAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaCrewAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaImageAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaReviewAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaVideoAdapter
import com.koniukhov.cinecircle.feature.media.details.dialog.FullscreenImageDialog
import com.koniukhov.cinecircle.feature.media.details.dialog.FullscreenVideoDialog
import com.koniukhov.cinecircle.feature.media.details.dialog.ReviewDetailBottomSheetDialog
import com.koniukhov.cinecircle.feature.media.details.ui.state.MovieDetailsUiState
import com.koniukhov.cinecircle.feature.media.details.ui.viewmodel.MovieDetailsViewModel
import com.koniukhov.cinecircle.feature.media.details.utils.MediaDetailsUtils
import com.koniukhov.cinecircle.feature.movie_details.R
import com.koniukhov.cinecircle.feature.movie_details.databinding.DialogAddToCollectionBinding
import com.koniukhov.cinecircle.feature.movie_details.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import com.koniukhov.cinecircle.core.design.R as design_R

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailsViewModel by viewModels()
    private lateinit var trailerAdapter: MediaVideoAdapter
    private lateinit var imageAdapter: MediaImageAdapter
    private lateinit var castAdapter: MediaCastAdapter
    private lateinit var crewAdapter: MediaCrewAdapter
    private lateinit var reviewAdapter: MediaReviewAdapter
    private lateinit var collectionAdapter: CollectionMediaAdapter
    private lateinit var recommendationsAdapter: MediaAdapter
    private lateinit var similarMoviesAdapter: MediaAdapter
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
        observeUiState()
    }

    private fun setupWindowInsets(view: View) {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        windowInsetsController = WindowCompat.getInsetsController(requireActivity().window, view)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun setupRecyclerViews() {
        with(binding){
            trailerAdapter = MediaVideoAdapter(
                lifecycle = lifecycle,
                onFullscreenEnter = { fullscreenView, exitFullscreen ->
                    handleEnterFullscreen(fullscreenView, exitFullscreen)
                },
                onFullscreenExit = {
                    handleExitFullscreen()
                }
            )
            recyclerTrailers.adapter = trailerAdapter

            imageAdapter = MediaImageAdapter { imagePath ->
                showFullscreenImage(imagePath)
            }
            recyclerImages.adapter = imageAdapter

            castAdapter = MediaCastAdapter()
            recyclerCast.adapter = castAdapter

            crewAdapter = MediaCrewAdapter()
            recyclerCrew.adapter = crewAdapter

            reviewAdapter = MediaReviewAdapter { review ->
                showReviewDetail(review)
            }
            recyclerReviews.adapter = reviewAdapter

            collectionAdapter = CollectionMediaAdapter { movieId ->
                findNavController().navigateToMovieDetails(movieId)
            }
            recyclerCollection.adapter = collectionAdapter

            recommendationsAdapter = MediaAdapter { id, _ ->
                findNavController().navigateToMovieDetails(id)
            }
            recyclerRecommendations.adapter = recommendationsAdapter

            similarMoviesAdapter = MediaAdapter { id, _ ->
                findNavController().navigateToMovieDetails(id)
            }
            recyclerSimilar.adapter = similarMoviesAdapter
        }
    }

    private fun showReviewDetail(review: MediaReview) {
        val dialog = ReviewDetailBottomSheetDialog.Companion.newInstance(review)
        dialog.show(parentFragmentManager, REVIEW_DETAIL_DIALOG_TAG)
    }

    private fun handleEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
        val dialog = FullscreenVideoDialog.newInstance(fullscreenView, exitFullscreen)
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
        val dialog = FullscreenImageDialog.newInstance(imagePath)
        dialog.show(parentFragmentManager, FULLSCREEN_IMAGE_DIALOG_TAG)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { uiState ->
                        if (!uiState.isLoading && uiState.error == null) {
                            hideSkeletons()
                            uiState.movieDetails?.let { movieDetails ->
                                updateMovieDetailsSection(movieDetails, uiState)
                            }
                            updateVideosSection(uiState.videos)
                            updateImagesSection(uiState.images)
                            updateCreditsSection(uiState.credits)
                            updateReviewsSection(uiState.reviews)
                            updateCollectionSection(uiState.collectionDetails)
                            updateRecommendationsSection(uiState.recommendations)
                            updateSimilarSection(uiState.similarMovies)
                        } else {
                            Timber.d(uiState.error)
                        }
                    }
                }

                launch {
                    viewModel.userRating.collect { rating ->
                        binding.ratingBar.rating = rating
                    }
                }
            }
        }

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                viewModel.setUserRating(rating)
            }
        }
    }

    private fun updateMovieDetailsSection(movieDetails: MovieDetails, uiState: MovieDetailsUiState) {
        with(binding){
            if (movieDetails.backdropPath.isNotEmpty()){
                imgBackdrop.load(TMDBEndpoints.IMAGE_URL_TEMPLATE.format(movieDetails.backdropPath)){
                    placeholder(design_R.drawable.placeholder_image)
                }
            }else {
                imgBackdrop.load(design_R.drawable.placeholder_image)
            }
            movieTitle.text = movieDetails.title
            rating.text = MediaDetailsUtils.formatRating(movieDetails.voteAverage)
            duration.text = MediaDetailsUtils.formatRuntime(
                runtime = movieDetails.runtime,
                hoursLabel = getString(R.string.hours_short),
                minutesLabel = getString(R.string.minutes_short)
            )
            age.text = MediaDetailsUtils.getAgeRating(movieDetails, uiState.releaseDates, viewModel.countryCode)
            country.text = MediaDetailsUtils.getMovieCountryCode(movieDetails)
            year.text = formatDate(movieDetails.releaseDate)
            if (movieDetails.overview.isNotEmpty()){
                plotDescription.text = movieDetails.overview
            }else{
                plotDescription.text = getString(R.string.not_available)
            }

        }
        setupGenres(movieDetails.genres)
        updateAboutSection(movieDetails)
    }

    private fun updateVideosSection(mediaVideos: MediaVideos?) {
        mediaVideos?.let {
            val youTubeTrailers = mediaVideos.getYouTubeTrailersAndTeasers()
            if (mediaVideos.results.isEmpty() || youTubeTrailers.isEmpty()) {
                binding.recyclerTrailers.visibility = View.GONE
                binding.containerNoTrailer.visibility = View.VISIBLE
            } else {
                trailerAdapter.setVideos(youTubeTrailers)
                binding.recyclerTrailers.visibility = View.VISIBLE
                binding.containerNoTrailer.visibility = View.GONE
            }
        }
    }

    private fun updateImagesSection(mediaImages: MediaImages?) {
        mediaImages?.let {
            val allImages = mutableListOf<Image>()
            allImages.addAll(mediaImages.backdrops)
            allImages.addAll(mediaImages.posters)
            if (allImages.isEmpty()){
                binding.recyclerImages.visibility = View.GONE
                binding.containerNoImages.visibility = View.VISIBLE
            } else {
                imageAdapter.setImages(allImages)
                binding.recyclerImages.visibility = View.VISIBLE
                binding.containerNoImages.visibility = View.GONE
            }
        }
    }

    private fun updateCreditsSection(credits: MediaCredits?) {
        credits?.cast?.let {
            if (it.isNotEmpty()){
                castAdapter.setCastMembers(it)
            }else{
                binding.recyclerCast.visibility = View.GONE
                binding.containerNoCast.visibility = View.VISIBLE
            }
        }
        credits?.crew?.let {
            if (it.isNotEmpty()){
                crewAdapter.setCrewMembers(it)
            }else{
                binding.recyclerCrew.visibility = View.GONE
                binding.containerNoCrew.visibility = View.VISIBLE
            }
        }
    }

    private fun updateReviewsSection(reviews: List<MediaReview>) {
        if (reviews.isEmpty()) {
            binding.recyclerReviews.visibility = View.GONE
            binding.containerNoReviews.visibility = View.VISIBLE
        } else {
            reviewAdapter.setReviews(reviews)
        }
    }

    private fun updateCollectionSection(collectionDetails: CollectionDetails?){
        collectionDetails?.let { collectionDetails ->
            if (collectionDetails.exists()){
                with(binding){
                    collectionTitle.visibility = View.VISIBLE
                    recyclerCollection.visibility = View.VISIBLE
                    collectionTitle.text = collectionDetails.name
                }
                collectionAdapter.setMovies(collectionDetails.parts)
            }
        }
    }

    private fun updateRecommendationsSection(recommendations: List<Movie>) {
        if (recommendations.isNotEmpty()){
            recommendationsAdapter.setMediaItems(recommendations)
        }else{
            binding.recyclerRecommendations.visibility = View.GONE
            binding.containerNoRecommendations.visibility = View.VISIBLE
        }
    }

    private fun updateSimilarSection(similarMovies: List<Movie>) {
        if (similarMovies.isNotEmpty()){
            similarMoviesAdapter.setMediaItems(similarMovies)
        }else{
            binding.recyclerSimilar.visibility = View.GONE
            binding.containerNoSimilar.visibility = View.VISIBLE
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
        val movieId = args.getInt(NavArgs.ARG_MOVIE_ID, INVALID_ID)
        viewModel.initMovie(movieId)
        viewModel.checkAndLoadCollections()
    }

    private fun setupToolbar() {
        setupNavigationClickListener()
        setupFavoriteButton()
    }

    private fun setupNavigationClickListener() {
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupFavoriteButton() {
        binding.btnFavorite.setOnClickListener {
            handleFavoriteButtonClick()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isMovieInCollection.collect { isInCollections ->
                updateFavoriteButtonState(isInCollections)
            }
        }
    }

    private fun updateFavoriteButtonState(isInCollections: Boolean) {
        binding.btnFavorite.apply {
            if (isInCollections) {
                setIconResource(design_R.drawable.ic_favorite_filled_24)
            } else {
                setIconResource(design_R.drawable.ic_favorite_24)
            }
        }
    }

    private fun handleFavoriteButtonClick() {
        viewLifecycleOwner.lifecycleScope.launch {
            val isInCollections = viewModel.isMovieInCollection.value

            if (isInCollections) {
                viewModel.removeMovieFromCollection()
                viewModel.removeMovieDetailsFromCache()
                showSnackbar(getString(R.string.movie_removed_from_collections))
            } else {
                showAddToCollectionDialog()
            }
        }
    }

    private fun showAddToCollectionDialog() {
        val dialogBinding = DialogAddToCollectionBinding.inflate(layoutInflater)

        val collections = viewModel.allCollections.value

        if (collections.isEmpty()) {
            showSnackbar(getString(R.string.no_collections_available))
            return
        }

        collections.forEach { collection ->
            val radioButton = MaterialRadioButton(requireContext())
            radioButton.text = collection.name
            radioButton.id = View.generateViewId()
            radioButton.tag = collection.id
            dialogBinding.radioGroupCollections.addView(radioButton)
        }

        if (collections.isNotEmpty()) {
            dialogBinding.radioGroupCollections.check(dialogBinding.radioGroupCollections.getChildAt(0).id)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.add) { _, _ ->
                val selectedId = dialogBinding.radioGroupCollections.checkedRadioButtonId
                if (selectedId != INVALID_ID) {
                    val selectedRadioButton = dialogBinding.radioGroupCollections.findViewById<MaterialRadioButton>(selectedId)
                    val collectionId = selectedRadioButton.tag as Long

                    viewLifecycleOwner.lifecycleScope.launch {
                        val collectionName = viewModel.addMovieToCollection(collectionId)
                        viewModel.cacheMovieDetails()
                        if (collectionName.isNotEmpty()) {
                            showSnackbar(getString(R.string.movie_added_to_collection, collectionName))
                        }
                    }
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun updateAboutSection(movieDetails: MovieDetails) {
        with(binding.sectionAbout) {
            budgetValue.text = if (movieDetails.budget > 0) {
                "$${String.format(Locale.US,"%,d", movieDetails.budget)}"
            } else {
                getString(R.string.not_available)
            }

            revenueValue.text = if (movieDetails.revenue > 0) {
                "$${String.format(Locale.US,"%,d", movieDetails.revenue)}"
            } else {
                getString(R.string.not_available)
            }

            originalTitleValue.text = movieDetails.originalTitle.ifEmpty {
                getString(R.string.not_available)
            }

            originalLanguageValue.text = viewModel.languages.entries.firstOrNull { it.value == movieDetails.originalLanguage }?.key ?: getString(R.string.not_available)

            statusValue.text = movieDetails.status.ifEmpty {
                getString(R.string.not_available)
            }

            popularityValue.text = String.format(Locale.US,"%.1f", movieDetails.popularity)

            productionCountriesValue.text = if (movieDetails.productionCountries.isNotEmpty()) {
                movieDetails.productionCountries.joinToString(", ") { it.name }
            } else {
                getString(R.string.not_available)
            }

            spokenLanguagesValue.text = if (movieDetails.spokenLanguages.isNotEmpty()) {
                movieDetails.spokenLanguages.filter { it.name.isNotEmpty() }.joinToString(", ") { it.name }
            } else {
                getString(R.string.not_available)
            }

            homepageValue.text = movieDetails.homePage.ifEmpty {
                getString(R.string.not_available)
            }

            if (movieDetails.homePage.isNotEmpty()) {
                homepageValue.setOnClickListener {
                    requireContext().openWebsite(movieDetails.homePage)
                }
                homepageValue.isClickable = true
            } else {
                homepageValue.setOnClickListener(null)
                homepageValue.isClickable = false
            }

            releaseDateValue.text = formatDate(movieDetails.releaseDate).ifEmpty {
                getString(R.string.not_available)
            }

            runtimeValue.text = if (movieDetails.runtime > 0) {
                MediaDetailsUtils.formatRuntime(
                    runtime = movieDetails.runtime,
                    hoursLabel = getString(R.string.hours_short),
                    minutesLabel = getString(R.string.minutes_short)
                )
            } else {
                getString(R.string.not_available)
            }

            taglineValue.text = movieDetails.tagline.ifEmpty {
                getString(R.string.not_available)
            }

            voteCountValue.text = String.format("%,d", movieDetails.voteCount)
        }
    }

    override fun onDestroyView() {
        currentExitFullscreenFunction?.invoke()
        fullscreenVideoDialog?.dismiss()
        fullscreenVideoDialog = null
        _binding = null
        super.onDestroyView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (isFullscreen) {
            when (newConfig.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    binding.appBarLayout.visibility = View.GONE
                }
                Configuration.ORIENTATION_PORTRAIT -> {
                    binding.appBarLayout.visibility = View.GONE
                }
            }
        } else {
            binding.appBarLayout.visibility = View.VISIBLE
        }
    }

    private fun setupSkeletons() {
        containerBackdropSkeleton = binding.skeletonBackdrop
        plotSkeleton = binding.skeletonPlot
        imagesRecyclerSkeleton = binding.recyclerImages.applySkeleton(R.layout.item_movie_image, RECYCLER_SKELETON_ITEM_COUNT)
        trailersRecyclerSkeleton = binding.recyclerTrailers.applySkeleton(R.layout.item_movie_image, RECYCLER_SKELETON_ITEM_COUNT)
        castRecyclerSkeleton = binding.recyclerCast.applySkeleton(R.layout.item_person, RECYCLER_SKELETON_ITEM_COUNT)
        crewRecyclerSkeleton = binding.recyclerCrew.applySkeleton(R.layout.item_person, RECYCLER_SKELETON_ITEM_COUNT)
        reviewsRecyclerSkeleton = binding.recyclerReviews.applySkeleton(R.layout.item_review, RECYCLER_SKELETON_ITEM_COUNT)
        recommendationsRecyclerSkeleton = binding.recyclerRecommendations.applySkeleton(design_R.layout.item_media, RECYCLER_SKELETON_ITEM_COUNT)
        similarRecyclerSkeleton = binding.recyclerSimilar.applySkeleton(design_R.layout.item_media, RECYCLER_SKELETON_ITEM_COUNT)
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