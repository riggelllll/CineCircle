package com.koniukhov.cinecircle.feature.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.feature.search.databinding.DialogFiltersBinding
import dagger.hilt.android.AndroidEntryPoint
import com.koniukhov.cinecircle.core.common.R as commonR


@AndroidEntryPoint
class FiltersDialogFragment() : DialogFragment() {

    private var _binding: DialogFiltersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupStyle()
    }

    override fun onStart() {
        super.onStart()
        setupDialogLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupViewPager()
        setupTabMediator()
    }

    private fun setupStyle() {
        setStyle(STYLE_NORMAL, R.style.Theme_CineCircle_FullScreenDialog)
    }

    private fun setupDialogLayout(){
        val dialog = getDialog()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { dismiss() }
    }

    private fun setupViewPager(){
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = setupAdapter()
    }

    private fun setupAdapter(): FragmentStateAdapter {
        return object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> MovieFiltersDialogFragment{ dismiss() }
                    1 -> TvSeriesFiltersDialogFragment()
                    else -> throw IllegalStateException()
                }
            }
        }
    }

    private fun setupTabMediator() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(commonR.string.movies_title)
                1 -> getString(commonR.string.tv_series_title)
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FiltersDialogFragment"
    }
}