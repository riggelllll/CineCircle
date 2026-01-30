package com.koniukhov.cinecirclex.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecirclex.core.common.Constants.IMAGE_RADIUS
import com.koniukhov.cinecirclex.core.design.R
import com.koniukhov.cinecirclex.core.design.databinding.ItemMediaBinding
import com.koniukhov.cinecirclex.core.domain.model.CollectionMedia
import com.koniukhov.cinecirclex.core.network.api.TMDBEndpoints.ImageSizes.POSTER_MEDIUM
import com.koniukhov.cinecirclex.core.ui.utils.CollectionMediaDiffCallback
import java.util.Locale

class CollectionMediaAdapter(
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CollectionMediaAdapter.MediaViewHolder>() {

    private var media: List<CollectionMedia> = emptyList()

    class MediaViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val item = media[position]

        holder.itemView.setOnClickListener {
            onItemClick(item.id)
        }

        with(holder.binding) {
            title.text = item.title
            rating.text = String.format(Locale.US,"%.1f", item.voteAverage)

            if (item.posterPath.isNotEmpty()) {
                poster.load(POSTER_MEDIUM.format(item.posterPath)) {
                    placeholder(R.drawable.placeholder_image)
                    transformations(RoundedCornersTransformation(IMAGE_RADIUS))
                }
            } else {
                poster.load(R.drawable.placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = media.size

    fun setMovies(newMedia: List<CollectionMedia>) {
        val diffCallback = CollectionMediaDiffCallback(media, newMedia)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        media = newMedia
        diffResult.dispatchUpdatesTo(this)
    }
}