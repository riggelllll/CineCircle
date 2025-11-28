package com.koniukhov.cinecircle.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.common.Constants.IMAGE_RADIUS
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.design.databinding.ItemMediaListBinding
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.core.ui.util.MediaDiffCallback
import java.util.Locale

class MediaListAdapter(
    private val onItemClick: (Int, MediaType) -> Unit
) : RecyclerView.Adapter<MediaListAdapter.MediaViewHolder>() {

    private var mediaItems: List<MediaItem> = emptyList()

    class MediaViewHolder(val binding: ItemMediaListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val mediaItem = mediaItems[position]

        holder.itemView.setOnClickListener {
            val type = if (mediaItem is Movie) MediaType.MOVIE else MediaType.TV_SERIES
            onItemClick(mediaItem.id, type)
        }

        with(holder.binding) {
            title.text = mediaItem.title
            rating.text = String.format(Locale.US,"%.1f", mediaItem.voteAverage)

            if (mediaItem.posterPath.isNotEmpty()) {
                poster.load(IMAGE_URL_TEMPLATE.format(mediaItem.posterPath)) {
                    placeholder(R.drawable.placeholder_image)
                    transformations(RoundedCornersTransformation(IMAGE_RADIUS))
                }
            } else {
                poster.load(R.drawable.placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = mediaItems.size

    fun setMediaItems(newItems: List<MediaItem>) {
        val diffCallback = MediaDiffCallback(mediaItems, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mediaItems = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}