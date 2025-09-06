package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.koniukhov.cinecircle.core.domain.model.Video
import com.koniukhov.cinecircle.feature.movie_details.databinding.ItemMovieTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

class MovieTrailersAdapter(
    private val lifecycle: Lifecycle,
    private val onFullscreenEnter: (View, () -> Unit) -> Unit,
    private val onFullscreenExit: () -> Unit
) : RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder>() {

    private val trailers = mutableListOf<Video>()

    fun setTrailers(newTrailers: List<Video>) {
        trailers.clear()
        val filteredTrailers = newTrailers.filter {
            it.site.equals("YouTube", ignoreCase = true) &&
            it.type.equals("Trailer", ignoreCase = true)
        }
        trailers.addAll(filteredTrailers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val binding = ItemMovieTrailerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrailerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bind(trailers[position])
    }

    override fun getItemCount(): Int = trailers.size

    inner class TrailerViewHolder(
        private val binding: ItemMovieTrailerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.youtubePlayerView.enableAutomaticInitialization = false

            lifecycle.addObserver(binding.youtubePlayerView)

            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build()

            binding.youtubePlayerView.addFullscreenListener(object : FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    onFullscreenEnter(fullscreenView, exitFullscreen)
                }

                override fun onExitFullscreen() {
                    onFullscreenExit()
                }
            })

            binding.youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(video.key, 0f)
                }
            }, iFramePlayerOptions)
        }
    }
}