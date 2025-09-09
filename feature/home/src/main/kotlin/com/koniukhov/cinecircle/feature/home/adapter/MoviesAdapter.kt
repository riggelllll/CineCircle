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
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.core.design.databinding.ItemMediaBinding
import java.util.Locale

class MoviesAdapter(
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>(){
    private var movies: List<Movie> = emptyList()

    class MoviesViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = movies[position]
        holder.itemView.setOnClickListener {
            onItemClick(movie.id)
        }
        holder.binding.title.text = movie.title
        holder.binding.rating.text = String.format(Locale.US,"%.1f", movie.voteAverage)
        if (movie.posterPath.isNotEmpty()){
            holder.binding.poster.load(IMAGE_URL_TEMPLATE.format(movie.posterPath)){
                placeholder(R.drawable.placeholder_image)
                transformations(RoundedCornersTransformation(IMAGE_RADIUS))
            }
        }else{
            holder.binding.poster.load(R.drawable.placeholder_image)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}