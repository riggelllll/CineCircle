package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import com.koniukhov.cinecircle.core.domain.model.MovieReview
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.movie_details.databinding.ItemReviewBinding
import com.koniukhov.cinecircle.core.design.R.drawable.placeholder_image as placeholder_image

class MovieReviewsAdapter(
    private val onReviewClick: (MovieReview) -> Unit
) : RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder>() {

    private var reviews: List<MovieReview> = emptyList()

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
                imgUserAvatar.load(IMAGE_URL_TEMPLATE.format(review.authorDetails.avatarPath)) {
                    placeholder(placeholder_image)
                }
            } else {
                imgUserAvatar.load(placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = reviews.size

    fun setReviews(newReviews: List<MovieReview>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}