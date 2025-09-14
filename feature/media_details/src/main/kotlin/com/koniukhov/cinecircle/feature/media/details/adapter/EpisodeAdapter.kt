package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.common.Constants.IMAGE_RADIUS
import com.koniukhov.cinecircle.core.common.util.DateUtils.formatDate
import com.koniukhov.cinecircle.core.design.R as design_R
import com.koniukhov.cinecircle.core.domain.model.TvEpisodeDetails
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.movie_details.R
import com.koniukhov.cinecircle.feature.movie_details.databinding.ItemEpisodeBinding

class EpisodeAdapter : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private var episodes: List<TvEpisodeDetails> = emptyList()
    private val expandedPositions = mutableSetOf<Int>()

    inner class EpisodeViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: TvEpisodeDetails, position: Int) {
            with(binding) {
                episodeTitle.text = itemView.context.getString(
                    R.string.episode_title_format,
                    episode.episodeNumber,
                    episode.name
                )

                airDate.text = formatDate(episode.airDate).ifEmpty {
                    itemView.context.getString(R.string.not_available)
                }

                runtime.text = if (episode.runtime > 0) {
                    itemView.context.getString(R.string.runtime_format, episode.runtime)
                } else {
                    itemView.context.getString(R.string.not_available)
                }

                overview.text = episode.overview.ifEmpty {
                    itemView.context.getString(R.string.not_available)
                }

                if (episode.stillPath.isNotEmpty()) {
                    imgEpisode.load(IMAGE_URL_TEMPLATE.format(episode.stillPath)) {
                        placeholder(design_R.drawable.placeholder_image)
                        transformations(RoundedCornersTransformation(IMAGE_RADIUS))
                    }
                } else {
                    imgEpisode.load(design_R.drawable.placeholder_image)
                }

                val isExpanded = expandedPositions.contains(position)
                overview.visibility = if (isExpanded) View.VISIBLE else View.GONE
                btnExpand.setImageResource(
                    if (isExpanded) design_R.drawable.ic_expand_less_24
                    else design_R.drawable.ic_expand_more_24
                )

                btnExpand.setOnClickListener {
                    if (isExpanded) {
                        expandedPositions.remove(position)
                    } else {
                        expandedPositions.add(position)
                    }
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(episodes[position], position)
    }

    override fun getItemCount(): Int = episodes.size

    fun setEpisodes(newEpisodes: List<TvEpisodeDetails>) {
        this.episodes = newEpisodes
        expandedPositions.clear()
        notifyDataSetChanged()
    }
}
