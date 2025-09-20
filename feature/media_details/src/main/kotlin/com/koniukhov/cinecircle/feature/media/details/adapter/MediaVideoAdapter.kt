package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.koniukhov.cinecircle.core.domain.model.Video
import com.koniukhov.cinecircle.feature.movie_details.BuildConfig
import com.koniukhov.cinecircle.feature.movie_details.databinding.ItemMovieTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import timber.log.Timber

class MediaVideoAdapter(
    private val lifecycle: Lifecycle,
    private val onFullscreenEnter: (View, () -> Unit) -> Unit,
    private val onFullscreenExit: () -> Unit
) : RecyclerView.Adapter<MediaVideoAdapter.VideoViewHolder>() {

    private val videos = mutableListOf<Video>()

    fun setVideos(newVideos: List<Video>) {
        videos.clear()
        videos.addAll(newVideos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemMovieTrailerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(lifecycle, binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.cueVideo(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    inner class VideoViewHolder(
        lifecycle: Lifecycle,
        binding: ItemMovieTrailerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var youTubePlayer: YouTubePlayer? = null
        private var currentVideo: Video? = null

        init {
            lifecycle.addObserver(binding.youtubePlayerView)

            binding.youtubePlayerView.enableAutomaticInitialization = false

            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .origin("https://${BuildConfig.LIBRARY_PACKAGE_NAME}")
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
                    this@VideoViewHolder.youTubePlayer = youTubePlayer
                    currentVideo?.let { youTubePlayer.cueVideo(it.key, 0f) }
                }

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) {
                    super.onError(youTubePlayer, error)
                    Timber.d(error.toString())
                }
            }, iFramePlayerOptions)
        }


        fun cueVideo(video: Video) {
            currentVideo = video
            youTubePlayer?.cueVideo(video.key, 0f)
        }
    }
}