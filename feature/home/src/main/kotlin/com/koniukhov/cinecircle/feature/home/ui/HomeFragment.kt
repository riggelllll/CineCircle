package com.koniukhov.cinecircle.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.koniukhov.cinecircle.core.common.R
import com.koniukhov.cinecircle.feature.home.R as homeR
import com.koniukhov.cinecircle.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

interface NetworkErrorListener {
    fun onNetworkError(fragmentType: Int)
}

@AndroidEntryPoint
class HomeFragment : Fragment(), NetworkErrorListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var tabLayoutMediator: TabLayoutMediator? = null
    private var pagerAdapter: FragmentStateAdapter? = null
    private var isDialogShowing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        setupTabMediator()
    }

    override fun onNetworkError(fragmentType: Int) {
        if (isDialogShowing) return

        isDialogShowing = true
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(homeR.string.network_error_title)
            .setMessage(homeR.string.network_error_message)
            .setCancelable(false)
            .setNegativeButton(homeR.string.exit) { _, _ ->
                isDialogShowing = false
                requireActivity().finish()
            }
            .setPositiveButton(homeR.string.retry) { _, _ ->
                isDialogShowing = false
                notifyChildFragmentsToRetry()
            }
            .setOnDismissListener {
                isDialogShowing = false
            }
            .show()
    }

    private fun notifyChildFragmentsToRetry() {
        childFragmentManager.fragments.forEach { fragment ->
            when (fragment) {
                is MoviesHomeFragment -> fragment.retryLoading()
                is TvSeriesHomeFragment -> fragment.retryLoading()
            }
        }
    }

    private fun setupViewPager(){
        binding.viewPager.isUserInputEnabled = false
        pagerAdapter = setupAdapter()
        binding.viewPager.adapter = pagerAdapter
    }

    private fun setupAdapter(): FragmentStateAdapter {
        return object : FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> MoviesHomeFragment()
                    1 -> TvSeriesHomeFragment()
                    else -> throw IllegalStateException()
                }
            }
        }
    }

    private fun setupTabMediator() {
        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.movies_title)
                1 -> getString(R.string.tv_series_title)
                else -> ""
            }
        }
        tabLayoutMediator?.attach()
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        pagerAdapter = null

        tabLayoutMediator?.detach()
        tabLayoutMediator = null


        _binding = null

        super.onDestroyView()
    }

    companion object {
        const val FRAGMENT_TYPE_MOVIES = 0
        const val FRAGMENT_TYPE_TV_SERIES = 1
    }
}