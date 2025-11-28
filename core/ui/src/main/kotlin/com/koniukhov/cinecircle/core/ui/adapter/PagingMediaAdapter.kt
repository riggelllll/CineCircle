package com.koniukhov.cinecircle.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.common.Constants
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.design.databinding.ItemMediaListBinding
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints
import com.koniukhov.cinecircle.core.ui.util.MediaItemCallback
import java.util.Locale

class PagingMediaAdapter(
    val onClick: (Int, MediaType) -> Unit
) : PagingDataAdapter<MediaItem, PagingMediaAdapter.MediaViewHolder>(MediaItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class MediaViewHolder(
        private val binding: ItemMediaListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaItem) {
            itemView.setOnClickListener {
                val mediaType = if (item is Movie) MediaType.MOVIE else MediaType.TV_SERIES
                onClick(item.id, mediaType)
            }

            binding.title.text = item.title
            binding.rating.text = String.Companion.format(Locale.US,"%.1f", item.voteAverage)

            if (item.posterPath.isNotEmpty()) {
                binding.poster.load(TMDBEndpoints.IMAGE_URL_TEMPLATE.format(item.posterPath)) {
                    placeholder(R.drawable.placeholder_image)
                    transformations(RoundedCornersTransformation(Constants.IMAGE_RADIUS))
                }
            } else {
                binding.poster.load(R.drawable.placeholder_image)
            }
        }
    }
}