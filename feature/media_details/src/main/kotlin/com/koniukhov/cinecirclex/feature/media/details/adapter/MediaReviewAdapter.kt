package com.koniukhov.cinecirclex.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import com.koniukhov.cinecirclex.core.design.R.drawable.placeholder_image
import com.koniukhov.cinecirclex.core.domain.model.MediaReview
import com.koniukhov.cinecirclex.core.network.api.TMDBEndpoints.ImageSizes.PROFILE_MEDIUM
import com.koniukhov.cinecirclex.core.ui.utils.MediaReviewDiffCallback
import com.koniukhov.cinecirclex.feature.movie_details.databinding.ItemReviewBinding

class MediaReviewAdapter(
    private val onReviewClick: (MediaReview) -> Unit
) : RecyclerView.Adapter<MediaReviewAdapter.ReviewViewHolder>() {

    private var reviews: List<MediaReview> = emptyList()

    class ReviewViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        holder.itemView.setOnClickListener {
            onReviewClick(review)
        }

        with(holder.binding) {
            username.text = review.authorDetails.username.ifEmpty { review.author }
            reviewText.text = review.content

            if (review.authorDetails.rating.isNotEmpty()) {
                rating.text = review.authorDetails.rating
                rating.visibility = View.VISIBLE
            } else {
                rating.visibility = View.GONE
            }

            if (review.authorDetails.avatarPath.isNotEmpty()) {
                imgUserAvatar.load(PROFILE_MEDIUM.format(review.authorDetails.avatarPath)) {
                    placeholder(placeholder_image)
                }
            } else {
                imgUserAvatar.load(placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = reviews.size

    fun setReviews(newReviews: List<MediaReview>) {
        val diffCallback = MediaReviewDiffCallback(reviews, newReviews)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        reviews = newReviews
        diffResult.dispatchUpdatesTo(this)
    }
}