package com.koniukhov.cinecircle.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.common.Constants.IMAGE_RADIUS
import com.koniukhov.cinecircle.core.common.Constants.MediaType
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.home.databinding.ItemMediaListBinding
import java.util.Locale

class PagingMediaAdapter(
    val onClick: (Int, Int) -> Unit
) : PagingDataAdapter<MediaItem, PagingMediaAdapter.MediaViewHolder>(DIFF) {

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
            binding.rating.text = String.format(Locale.US,"%.1f", item.voteAverage)

            if (item.posterPath.isNotEmpty()) {
                binding.poster.load(IMAGE_URL_TEMPLATE.format(item.posterPath)) {
                    placeholder(R.drawable.placeholder_image)
                    transformations(RoundedCornersTransformation(IMAGE_RADIUS))
                }
            } else {
                binding.poster.load(R.drawable.placeholder_image)
            }
        }
    }


    companion object {
        val DIFF = object : DiffUtil.ItemCallback<MediaItem>() {
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem) = oldItem == newItem
        }
    }
}