package com.koniukhov.cinecircle.feature.search.ui

import androidx.fragment.app.Fragment
import com.koniukhov.cinecircle.feature.search.databinding.FragmentTvSeriesFiltersBinding

class TvSeriesFiltersDialogFragment : Fragment() {
    private var _binding: FragmentTvSeriesFiltersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View {
        _binding = FragmentTvSeriesFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}