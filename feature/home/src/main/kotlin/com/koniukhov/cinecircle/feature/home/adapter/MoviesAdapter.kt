package com.koniukhov.cinecircle.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.common.util.movieGenreMap
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.home.databinding.ItemHomeMovieBinding

const val IMAGE_RADIUS = 20f

class MoviesAdapter(private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>(){
    private var movies: List<Movie> = emptyList()

    class MoviesViewHolder(val binding: ItemHomeMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = ItemHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = movies[position]
        holder.itemView.setOnClickListener {
            onItemClick(movie.id)
        }
        holder.binding.title.text = movie.title
        holder.binding.genre.text = movieGenreMap[movie.genreIds[0]]
        holder.binding.rating.text = String.format("%.1f", movie.voteAverage).replace(',', '.')
        holder.binding.poster.load(IMAGE_URL_TEMPLATE.format(movie.posterPath)){
            placeholder(R.drawable.placeholder_image)
            transformations(RoundedCornersTransformation(IMAGE_RADIUS))
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(data: List<Movie>) {
        movies = data
        notifyDataSetChanged()
    }
}