package com.koniukhov.cinecircle.feature.media.details.ui

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
import com.koniukhov.cinecircle.core.common.navigation.navigateToTvSeriesDetails
import com.koniukhov.cinecircle.core.common.util.DateUtils.formatDate
import com.koniukhov.cinecircle.core.domain.model.Genre
import com.koniukhov.cinecircle.core.domain.model.Image
import com.koniukhov.cinecircle.core.domain.model.MediaCredits
import com.koniukhov.cinecircle.core.domain.model.MediaImages
import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.domain.model.MediaVideos
import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.domain.model.TvSeriesDetails
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints
import com.koniukhov.cinecircle.core.ui.adapter.MediaAdapter
import com.koniukhov.cinecircle.core.ui.utils.openWebsite
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaCastAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaCrewAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaImageAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaReviewAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.MediaVideoAdapter
import com.koniukhov.cinecircle.feature.media.details.adapter.SeasonAdapter
import com.koniukhov.cinecircle.feature.media.details.dialog.EpisodesBottomSheetDialog
import com.koniukhov.cinecircle.feature.media.details.dialog.FullscreenImageDialog
import com.koniukhov.cinecircle.feature.media.details.dialog.FullscreenVideoDialog
import com.koniukhov.cinecircle.feature.media.details.dialog.ReviewDetailBottomSheetDialog
import com.koniukhov.cinecircle.feature.media.details.ui.state.TvSeriesDetailsUiState
import com.koniukhov.cinecircle.feature.media.details.ui.viewmodel.TvSeriesDetailsViewModel
import com.koniukhov.cinecircle.feature.media.details.utils.MediaDetailsUtils
import com.koniukhov.cinecircle.feature.movie_details.R
import com.koniukhov.cinecircle.feature.movie_details.databinding.DialogAddToCollectionBinding
import com.koniukhov.cinecircle.feature.movie_details.databinding.FragmentTvSeriesDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import com.koniukhov.cinecircle.core.design.R as design_R

@AndroidEntryPoint
class TvSeriesDetailsFragment : Fragment() {
    private var _binding: FragmentTvSeriesDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TvSeriesDetailsViewModel by viewModels()
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private lateinit var imagesAdapter: MediaImageAdapter
    private lateinit var trailersAdapter: MediaVideoAdapter
    private lateinit var castAdapter: MediaCastAdapter
    private lateinit var crewAdapter: MediaCrewAdapter
    private lateinit var reviewAdapter: MediaReviewAdapter
    private lateinit var recommendationsAdapter: MediaAdapter
    private lateinit var similarMoviesAdapter: MediaAdapter
    private lateinit var seasonAdapter: SeasonAdapter
    private var fullscreenVideoDialog: FullscreenVideoDialog? = null
    private var isFullscreen = false
    private var currentExitFullscreenFunction: (() -> Unit)? = null

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
    private lateinit var seasonsRecyclerSkeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArgs()
        viewModel.loadTvSeriesDetails()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvSeriesDetailsBinding.inflate(inflater, container, false)
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

    private fun setupToolbar() {
        setupNavigationClickListener()
        setupFavoriteButton()
    }

    private fun setupFavoriteButton() {
        binding.btnFavorite.setOnClickListener {
            handleFavoriteButtonClick()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isTvSeriesInCollection.collect { isInCollections ->
                updateFavoriteButtonState(isInCollections)
            }
        }
    }

    private fun handleFavoriteButtonClick() {
        viewLifecycleOwner.lifecycleScope.launch {
            val isInCollections = viewModel.isTvSeriesInCollection.value

            if (isInCollections) {
                viewModel.removeTvSeriesFromCollection()
                viewModel.removeTvSeriesDetailsFromCache()
                showSnackbar(getString(R.string.tv_series_removed_from_collections))
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
                        val collectionName = viewModel.addTvSeriesToCollection(collectionId)
                        viewModel.cacheTvSeriesDetails()
                        if (collectionName.isNotEmpty()) {
                            showSnackbar(getString(R.string.tv_series_added_to_collection, collectionName))
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


    private fun updateFavoriteButtonState(isInCollections: Boolean) {
        binding.btnFavorite.apply {
            if (isInCollections) {
                setIconResource(design_R.drawable.ic_favorite_filled_24)
            } else {
                setIconResource(design_R.drawable.ic_favorite_24)
            }
        }
    }

    private fun setupNavigationClickListener() {
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupSkeletons() {
        containerBackdropSkeleton = binding.skeletonBackdrop
        plotSkeleton = binding.skeletonPlot
        imagesRecyclerSkeleton = binding.recyclerImages.applySkeleton(R.layout.item_movie_image,
            RECYCLER_SKELETON_ITEM_COUNT
        )
        trailersRecyclerSkeleton = binding.recyclerTrailers.applySkeleton(R.layout.item_movie_image,
            RECYCLER_SKELETON_ITEM_COUNT
        )
        castRecyclerSkeleton = binding.recyclerCast.applySkeleton(R.layout.item_person,
            RECYCLER_SKELETON_ITEM_COUNT
        )
        crewRecyclerSkeleton = binding.recyclerCrew.applySkeleton(R.layout.item_person,
            RECYCLER_SKELETON_ITEM_COUNT
        )
        reviewsRecyclerSkeleton = binding.recyclerReviews.applySkeleton(R.layout.item_review,
            RECYCLER_SKELETON_ITEM_COUNT
        )
        recommendationsRecyclerSkeleton = binding.recyclerRecommendations.applySkeleton(design_R.layout.item_media,
            RECYCLER_SKELETON_ITEM_COUNT
        )
        similarRecyclerSkeleton = binding.recyclerSimilar.applySkeleton(design_R.layout.item_media,
            RECYCLER_SKELETON_ITEM_COUNT
        )
        aboutSkeleton = binding.skeletonAbout

        seasonsRecyclerSkeleton = binding.recyclerSeasons.applySkeleton(design_R.layout.item_media,
            RECYCLER_SKELETON_ITEM_COUNT
        )
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
        seasonsRecyclerSkeleton.showSkeleton()
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
        seasonsRecyclerSkeleton.showOriginal()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { uiState ->
                        if (!uiState.isLoading && uiState.error == null) {
                            hideSkeletons()
                            uiState.details?.let { details ->
                                updateDetailsSection(details, uiState)
                            }
                            updateVideosSection(uiState.videos)
                            updateImagesSection(uiState.images)
                            updateCreditsSection(uiState.credits)
                            updateReviewsSection(uiState.reviews)
                            updateSeasons(uiState.seasons)
                            updateRecommendationsSection(uiState.recommendations)
                            updateSimilarSection(uiState.similar)
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

    private fun updateDetailsSection(details: TvSeriesDetails, uiState: TvSeriesDetailsUiState) {
        with(binding){
            if (details.backdropPath.isNotEmpty()){
                imgBackdrop.load(TMDBEndpoints.IMAGE_URL_TEMPLATE.format(details.backdropPath)){
                    placeholder(design_R.drawable.placeholder_image)
                }
            }else {
                imgBackdrop.load(design_R.drawable.placeholder_image)
            }
            title.text = details.name
            rating.text = MediaDetailsUtils.formatRating(details.voteAverage)
            val seasonsText = resources.getQuantityString(
                R.plurals.seasons_count,
                details.numberOfSeasons,
                details.numberOfSeasons
            )
            seasons.text = seasonsText
            age.text = MediaDetailsUtils.getTvSeriesAgeRating(uiState.contentRatings,  viewModel.countryCode)
            country.text = MediaDetailsUtils.getTvSeriesCountryCode(details)
            year.text = formatDate(details.firstAirDate)
            if (details.overview.isNotEmpty()){
                plotDescription.text = details.overview
            }else{
                plotDescription.text = getString(R.string.not_available)
            }

        }
        setupGenres(details.genres)
        updateAboutSection(details)
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

    private fun updateImagesSection(mediaImages: MediaImages?) {
        mediaImages?.let {
            val allImages = mutableListOf<Image>()
            allImages.addAll(mediaImages.backdrops)
            allImages.addAll(mediaImages.posters)
            if (allImages.isEmpty()){
                binding.recyclerImages.visibility = View.GONE
                binding.containerNoImages.visibility = View.VISIBLE
            } else {
                imagesAdapter.setImages(allImages)
                binding.recyclerImages.visibility = View.VISIBLE
                binding.containerNoImages.visibility = View.GONE
            }
        }
    }

    private fun updateVideosSection(mediaVideos: MediaVideos?) {
        mediaVideos?.let {
            val youTubeTrailers = mediaVideos.getYouTubeTrailersAndTeasers()
            if (mediaVideos.results.isEmpty() || youTubeTrailers.isEmpty()) {
                binding.recyclerTrailers.visibility = View.GONE
                binding.containerNoTrailer.visibility = View.VISIBLE
            } else {
                trailersAdapter.setVideos(youTubeTrailers)
                binding.recyclerTrailers.visibility = View.VISIBLE
                binding.containerNoTrailer.visibility = View.GONE
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

    private fun updateRecommendationsSection(recommendations: List<TvSeries>) {
        if (recommendations.isNotEmpty()){
            recommendationsAdapter.setMediaItems(recommendations)
        }else{
            binding.recyclerRecommendations.visibility = View.GONE
            binding.containerNoRecommendations.visibility = View.VISIBLE
        }
    }

    private fun updateSimilarSection(similarMovies: List<TvSeries>) {
        if (similarMovies.isNotEmpty()){
            similarMoviesAdapter.setMediaItems(similarMovies)
        }else{
            binding.recyclerSimilar.visibility = View.GONE
            binding.containerNoSimilar.visibility = View.VISIBLE
        }
    }

    private fun updateAboutSection(tvSeriesDetails: TvSeriesDetails) {
        with(binding.sectionAbout) {
            createdByValue.text = if (tvSeriesDetails.createdBy.isNotEmpty()) {
                tvSeriesDetails.createdBy.joinToString(", ") { it.name }
            } else {
                getString(R.string.not_available)
            }

            firstAirDateValue.text = formatDate(tvSeriesDetails.firstAirDate).ifEmpty {
                getString(R.string.not_available)
            }

            lastAirDateValue.text = formatDate(tvSeriesDetails.lastAirDate).ifEmpty {
                getString(R.string.not_available)
            }

            homepageValue.text = tvSeriesDetails.homepage.ifEmpty {
                getString(R.string.not_available)
            }

            if (tvSeriesDetails.homepage.isNotEmpty()) {
                homepageValue.setOnClickListener {
                    requireContext().openWebsite(tvSeriesDetails.homepage)
                }
                homepageValue.isClickable = true
            } else {
                homepageValue.setOnClickListener(null)
                homepageValue.isClickable = false
            }

            spokenLanguagesValue.text = if (tvSeriesDetails.spokenLanguages.isNotEmpty()) {
                tvSeriesDetails.spokenLanguages.filter { it.name.isNotEmpty() }.joinToString(", ") { it.name }
            } else {
                getString(R.string.not_available)
            }

            networksValue.text = if (tvSeriesDetails.networks.isNotEmpty()) {
                tvSeriesDetails.networks.joinToString(", ") { it.name }
            } else {
                getString(R.string.not_available)
            }

            numberOfSeasonsValue.text = tvSeriesDetails.numberOfSeasons.toString()

            numberOfEpisodesValue.text = tvSeriesDetails.numberOfEpisodes.toString()

            originalTitleValue.text = tvSeriesDetails.originalName.ifEmpty {
                getString(R.string.not_available)
            }

            originalLanguageValue.text = viewModel.languages.entries.firstOrNull { it.value == tvSeriesDetails.originalLanguage }?.key ?: getString(R.string.not_available)

            popularityValue.text = String.format(Locale.US,"%.1f", tvSeriesDetails.popularity)

            productionCountriesValue.text = if (tvSeriesDetails.productionCountries.isNotEmpty()) {
                tvSeriesDetails.productionCountries.joinToString(", ") { it.name }
            } else {
                getString(R.string.not_available)
            }

            statusValue.text = tvSeriesDetails.status.ifEmpty {
                getString(R.string.not_available)
            }

            taglineValue.text = tvSeriesDetails.tagline.ifEmpty {
                getString(R.string.not_available)
            }

            typeValue.text = tvSeriesDetails.type.ifEmpty {
                getString(R.string.not_available)
            }

            voteCountValue.text = String.format("%,d", tvSeriesDetails.voteCount)
        }
    }

    private fun updateSeasons(seasons: List<TvSeasonDetails>) {
        seasonAdapter.setSeasonsDetails(seasons)
    }

    private fun setupRecyclerViews() {
        with(binding){
            trailersAdapter = MediaVideoAdapter(
                lifecycle = lifecycle,
                onFullscreenEnter = { fullscreenView, exitFullscreen ->
                    handleEnterFullscreen(fullscreenView, exitFullscreen)
                },
                onFullscreenExit = {
                    handleExitFullscreen()
                }
            )
            recyclerTrailers.adapter = trailersAdapter
            imagesAdapter = MediaImageAdapter { imagePath ->
                showFullscreenImage(imagePath)
            }
            recyclerImages.adapter = imagesAdapter
            castAdapter = MediaCastAdapter()
            recyclerCast.adapter = castAdapter

            crewAdapter = MediaCrewAdapter()
            recyclerCrew.adapter = crewAdapter

            reviewAdapter = MediaReviewAdapter { review ->
                showReviewDetail(review)
            }
            recyclerReviews.adapter = reviewAdapter

            recommendationsAdapter = MediaAdapter { id, _ ->
                findNavController().navigateToTvSeriesDetails(id)
            }
            recyclerRecommendations.adapter = recommendationsAdapter

            similarMoviesAdapter = MediaAdapter { id, _ ->
                findNavController().navigateToTvSeriesDetails(id)
            }
            recyclerSimilar.adapter = similarMoviesAdapter

            seasonAdapter = SeasonAdapter { seasonDetails ->
                showEpisodesDialog(seasonDetails)
            }
            recyclerSeasons.adapter = seasonAdapter
        }
    }
    private fun showFullscreenImage(imagePath: String) {
        val dialog = FullscreenImageDialog.newInstance(imagePath)
        dialog.show(parentFragmentManager, FULLSCREEN_IMAGE_DIALOG_TAG)
    }

    private fun handleEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
        val dialog = FullscreenVideoDialog.newInstance(fullscreenView, exitFullscreen)
        fullscreenVideoDialog = dialog
        dialog.show(parentFragmentManager,
            FULLSCREEN_VIDEO_DIALOG_TAG
        )
        isFullscreen = true
        currentExitFullscreenFunction = exitFullscreen
    }

    private fun handleExitFullscreen() {
        isFullscreen = false
        currentExitFullscreenFunction = null
        fullscreenVideoDialog?.dismiss()
        fullscreenVideoDialog = null
    }

    private fun showReviewDetail(review: MediaReview) {
        val dialog = ReviewDetailBottomSheetDialog.Companion.newInstance(review)
        dialog.show(parentFragmentManager, REVIEW_DETAIL_DIALOG_TAG)
    }

    private fun showEpisodesDialog(seasonDetails: TvSeasonDetails) {
        val dialog = EpisodesBottomSheetDialog.newInstance()
        dialog.setSeasonDetails(seasonDetails)
        dialog.show(parentFragmentManager, EPISODES_DIALOG_TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupArgs(){
        val args = requireArguments()
        val tvSeriesId = args.getInt(NavArgs.ARG_TV_SERIES_ID, INVALID_ID)
        viewModel.initTvSeries(tvSeriesId)
        viewModel.checkAndLoadCollections()
    }

    companion object{
        private const val FULLSCREEN_IMAGE_DIALOG_TAG = "FullscreenImageDialog"
        private const val FULLSCREEN_VIDEO_DIALOG_TAG = "FullscreenVideoDialog"
        private const val REVIEW_DETAIL_DIALOG_TAG = "ReviewDetailDialog"
        private const val EPISODES_DIALOG_TAG = "EpisodesDialog"
        private const val RECYCLER_SKELETON_ITEM_COUNT = 5
    }
}