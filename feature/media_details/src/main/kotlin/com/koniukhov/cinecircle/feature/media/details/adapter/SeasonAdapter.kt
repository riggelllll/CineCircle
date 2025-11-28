package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.common.Constants.IMAGE_RADIUS
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.design.databinding.ItemMediaBinding
import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.core.ui.utils.TvSeasonDetailsDiffCallback
import java.util.Locale

class SeasonAdapter(
    private val onItemClick: (TvSeasonDetails) -> Unit
) : RecyclerView.Adapter<SeasonAdapter.SeasonsViewHolder>() {

    private var seasonDetails: List<TvSeasonDetails> = emptyList()

    class SeasonsViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonsViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeasonsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeasonsViewHolder, position: Int) {
        val details = seasonDetails[position]

        holder.itemView.setOnClickListener {
            onItemClick(details)
        }

        with(holder.binding) {
            title.text = details.name
            rating.text = String.format(Locale.US,"%.1f", details.voteAverage)

            if (details.posterPath.isNotEmpty()) {
                poster.load(IMAGE_URL_TEMPLATE.format(details.posterPath)) {
                    placeholder(R.drawable.placeholder_image)
                    transformations(RoundedCornersTransformation(IMAGE_RADIUS))
                }
            } else {
                poster.load(R.drawable.placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = seasonDetails.size

    fun setSeasonsDetails(newSeasons: List<TvSeasonDetails>) {
        val diffCallback = TvSeasonDetailsDiffCallback(seasonDetails, newSeasons)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        seasonDetails = newSeasons
        diffResult.dispatchUpdatesTo(this)
    }
}