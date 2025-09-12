package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.common.Constants.IMAGE_RADIUS
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.design.databinding.ItemMediaBinding
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import java.util.Locale

class MediaAdapter(
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private var mediaItems: List<MediaItem> = emptyList()

    class MediaViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val movie = mediaItems[position]

        holder.itemView.setOnClickListener {
            onItemClick(movie.id)
        }

        with(holder.binding) {
            title.text = movie.title
            rating.text = String.format(Locale.US,"%.1f", movie.voteAverage)

            if (movie.posterPath.isNotEmpty()) {
                poster.load(IMAGE_URL_TEMPLATE.format(movie.posterPath)) {
                    placeholder(R.drawable.placeholder_image)
                    transformations(RoundedCornersTransformation(IMAGE_RADIUS))
                }
            } else {
                poster.load(R.drawable.placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = mediaItems.size

    fun setMediaItems(movies: List<MediaItem>) {
        this.mediaItems = movies
        notifyDataSetChanged()
    }
}