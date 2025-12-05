package com.koniukhov.cinecircle.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.koniukhov.cinecircle.core.common.R
import com.koniukhov.cinecircle.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var tabLayoutMediator: TabLayoutMediator? = null
    private var pagerAdapter: FragmentStateAdapter? = null

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
}