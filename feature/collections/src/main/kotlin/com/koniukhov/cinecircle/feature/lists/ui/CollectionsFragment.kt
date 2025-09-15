package com.koniukhov.cinecircle.feature.lists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.koniukhov.cinecircle.core.database.model.MediaListWithCount
import com.koniukhov.cinecircle.feature.collections.databinding.CollectionsFragmentBinding
import com.koniukhov.cinecircle.feature.lists.adapter.CollectionsAdapter
import com.koniukhov.cinecircle.feature.lists.viewmodel.CollectionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionsFragment : Fragment() {

    private var _binding: CollectionsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CollectionsViewModel by viewModels()
    private lateinit var collectionsAdapter: CollectionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CollectionsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        collectionsAdapter = CollectionsAdapter { collection ->
            onCollectionClick(collection)
        }

        binding.recyclerCollections.apply {
            adapter = collectionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.collections.collect { collections ->
                collectionsAdapter.submitList(collections)
            }
        }
    }

    private fun onCollectionClick(collection: MediaListWithCount) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}