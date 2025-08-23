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
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.home.databinding.ItemMediaListBinding

class PagingMediaAdapter(val onClick: (Int) -> Unit) : PagingDataAdapter<MediaItem, RecyclerView.ViewHolder>(DIFF) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Movie -> TYPE_MOVIE
            is TvSeries -> TYPE_TV
            else -> error("Unknown type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMediaListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            TYPE_MOVIE -> MovieViewHolder(binding)
            TYPE_TV -> TvViewHolder(binding)
            else -> error("Unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> holder.bind(getItem(position) as Movie)
            is TvViewHolder -> holder.bind(getItem(position) as TvSeries)
        }
    }

    abstract class BaseMediaViewHolder(
        protected val binding: ItemMediaListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        protected fun bindCommon(
            title: String,
            voteAverage: Float,
            posterPath: String?
        ) {
            binding.title.text = title
            binding.rating.text = String.format("%.1f", voteAverage).replace(',', '.')
            if (posterPath != null && posterPath.isNotEmpty()) {
                binding.poster.load(IMAGE_URL_TEMPLATE.format(posterPath)) {
                    placeholder(R.drawable.placeholder_image)
                    transformations(RoundedCornersTransformation(IMAGE_RADIUS))
                }
            }
        }
    }

    inner class MovieViewHolder(
        binding: ItemMediaListBinding
    ) : BaseMediaViewHolder(binding) {
        fun bind(item: Movie) {
            itemView.setOnClickListener {
                onClick(item.id)
            }
            bindCommon(item.title, item.voteAverage, item.posterPath)
        }
    }

    inner class TvViewHolder(
        binding: ItemMediaListBinding
    ) : BaseMediaViewHolder(binding) {
        fun bind(item: TvSeries) {
            itemView.setOnClickListener {
                onClick(item.id)
            }
            bindCommon(item.name, item.voteAverage, item.posterPath)
        }
    }

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_TV = 1

        val DIFF = object : DiffUtil.ItemCallback<MediaItem>() {
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem) = oldItem == newItem
        }
    }
}