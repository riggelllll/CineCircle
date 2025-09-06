package com.koniukhov.cinecircle.feature.movie.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.domain.model.Movie
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.core.design.databinding.ItemMediaBinding

private const val IMAGE_RADIUS = 20f

class MovieRecommendationsAdapter(
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MovieRecommendationsAdapter.MovieViewHolder>() {

    private var movies: List<Movie> = emptyList()

    class MovieViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.itemView.setOnClickListener {
            onItemClick(movie.id)
        }

        with(holder.binding) {
            title.text = movie.title
            rating.text = String.format("%.1f", movie.voteAverage).replace(',', '.')

            if (movie.posterPath.isNotEmpty()) {
                poster.load(IMAGE_URL_TEMPLATE.format(movie.posterPath)) {
                    placeholder(R.drawable.placeholder_image)
                    transformations(RoundedCornersTransformation(IMAGE_RADIUS))
                }
            } else {
                poster.load(R.drawable.placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = movies.size

    fun setMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}