package com.koniukhov.cinecircle.feature.media.details.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.feature.media.details.adapter.EpisodeAdapter
import com.koniukhov.cinecircle.feature.movie_details.R
import com.koniukhov.cinecircle.feature.movie_details.databinding.BottomSheetEpisodesBinding

class EpisodesBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEpisodesBinding? = null
    private val binding get() = _binding!!

    private lateinit var episodeAdapter: EpisodeAdapter
    private var seasonDetails: TvSeasonDetails? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupData()
    }

    private fun setupRecyclerView() {
        episodeAdapter = EpisodeAdapter()
        binding.recyclerEpisodes.adapter = episodeAdapter
    }

    private fun setupData() {
        seasonDetails?.let { season ->
            with(binding) {
                seasonTitle.text = season.name
                episodesCount.text = getString(
                    R.string.episodes_count,
                    season.episodes.size
                )
                episodeAdapter.setEpisodes(season.episodes)
            }
        }
    }

    fun setSeasonDetails(season: TvSeasonDetails) {
        this.seasonDetails = season
        if (_binding != null) {
            setupData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): EpisodesBottomSheetDialog {
            return EpisodesBottomSheetDialog()
        }
    }
}