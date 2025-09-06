package com.koniukhov.cinecircle.feature.media.details.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil3.load
import coil3.request.placeholder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.koniukhov.cinecircle.core.domain.model.MovieReview
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.movie_details.databinding.BottomSheetReviewDetailBinding
import java.text.SimpleDateFormat
import java.util.Locale

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
        val review = arguments?.getSerializable(ARG_REVIEW) as? MovieReview
        review?.let { setupReviewData(it) }
    }

    private fun setupReviewData(review: MovieReview) {
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
                imgUserAvatar.load(IMAGE_URL_TEMPLATE.format(review.authorDetails.avatarPath)) {
                    placeholder(com.koniukhov.cinecircle.core.design.R.drawable.placeholder_image)
                }
            } else {
                imgUserAvatar.load(com.koniukhov.cinecircle.core.design.R.drawable.placeholder_image)
            }
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_REVIEW = "arg_review"

        fun newInstance(review: MovieReview): ReviewDetailBottomSheetDialog {
            return ReviewDetailBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_REVIEW, review)
                }
            }
        }
    }
}