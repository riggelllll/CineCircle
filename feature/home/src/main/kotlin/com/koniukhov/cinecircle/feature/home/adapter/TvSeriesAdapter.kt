package com.koniukhov.cinecircle.feature.home.adapter

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
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE

class TvSeriesAdapter(
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<TvSeriesAdapter.TvSeriesViewHolder>(){
    private var tvSeries: List<TvSeries> = emptyList()

    class TvSeriesViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvSeriesViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvSeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TvSeriesViewHolder, position: Int) {
        val tvSeries = tvSeries[position]
        holder.itemView.setOnClickListener {
            onItemClick(tvSeries.id)
        }
        holder.binding.title.text = tvSeries.name
        holder.binding.rating.text = String.format("%.1f", tvSeries.voteAverage).replace(',', '.')
        if (tvSeries.posterPath.isNotEmpty()){
            holder.binding.poster.load(IMAGE_URL_TEMPLATE.format(tvSeries.posterPath)){
                placeholder(R.drawable.placeholder_image)
                transformations(RoundedCornersTransformation(IMAGE_RADIUS))
            }
        }else{
            holder.binding.poster.load(R.drawable.placeholder_image)
        }
    }

    override fun getItemCount(): Int {
        return tvSeries.size
    }

    fun setData(tvSeries: List<TvSeries>) {
        this.tvSeries = tvSeries
        notifyDataSetChanged()
    }
}