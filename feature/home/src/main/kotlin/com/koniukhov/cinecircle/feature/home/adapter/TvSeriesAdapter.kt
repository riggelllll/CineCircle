package com.koniukhov.cinecircle.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.common.model.GenreUi
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.domain.model.TvSeries
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.home.databinding.ItemHomeMovieBinding

class TvSeriesAdapter(private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<TvSeriesAdapter.TvSeriesViewHolder>(){
    private var tvSeries: List<TvSeries> = emptyList()
    private var genres: List<GenreUi> = emptyList()

    class TvSeriesViewHolder(val binding: ItemHomeMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvSeriesViewHolder {
        val binding = ItemHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvSeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TvSeriesViewHolder, position: Int) {
        val movie = tvSeries[position]
        holder.itemView.setOnClickListener {
            onItemClick(movie.id)
        }
        holder.binding.title.text = movie.name
        holder.binding.genre.text = genres.find { it.id == movie.genreIds[0] }?.name
        holder.binding.rating.text = String.format("%.1f", movie.voteAverage).replace(',', '.')
        holder.binding.poster.load(IMAGE_URL_TEMPLATE.format(movie.posterPath)){
            placeholder(R.drawable.placeholder_image)
            transformations(RoundedCornersTransformation(IMAGE_RADIUS))
        }
    }

    override fun getItemCount(): Int {
        return tvSeries.size
    }

    fun setData(tvSeries: List<TvSeries>, genres: List<GenreUi>) {
        this.tvSeries = tvSeries
        this.genres = genres
        notifyDataSetChanged()
    }
}