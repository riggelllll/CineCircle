package com.koniukhov.cinecircle.feature.media.details.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil3.load
import coil3.request.placeholder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.koniukhov.cinecircle.core.common.util.DateUtils.formatDate
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.ImageSizes.PROFILE_MEDIUM
import com.koniukhov.cinecircle.feature.movie_details.databinding.BottomSheetReviewDetailBinding

class ReviewDetailBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: BottomSheetReviewDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetReviewDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReviewFromArguments()
    }

    private fun initReviewFromArguments() {
        val review = arguments?.getSerializable(ARG_REVIEW) as? MediaReview
        review?.let { setupReviewData(it) }
    }

    private fun setupReviewData(review: MediaReview) {
        with(binding) {
            username.text = review.authorDetails.username.ifEmpty { review.author }
            reviewText.text = review.content

            if (review.authorDetails.rating.isNotEmpty()) {
                rating.text = review.authorDetails.rating
                rating.visibility = View.VISIBLE
            } else {
                rating.visibility = View.GONE
            }

            createdAt.text = formatDate(review.createdAt)

            if (review.authorDetails.avatarPath.isNotEmpty()) {
                imgUserAvatar.load(PROFILE_MEDIUM.format(review.authorDetails.avatarPath)) {
                    placeholder(R.drawable.placeholder_image)
                }
            } else {
                imgUserAvatar.load(R.drawable.placeholder_image)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_REVIEW = "arg_review"

        fun newInstance(review: MediaReview): ReviewDetailBottomSheetDialog {
            return ReviewDetailBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_REVIEW, review)
                }
            }
        }
    }
}