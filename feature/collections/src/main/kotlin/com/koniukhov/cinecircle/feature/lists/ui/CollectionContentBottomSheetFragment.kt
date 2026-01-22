package com.koniukhov.cinecircle.feature.lists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.koniukhov.cinecircle.core.common.Constants.INVALID_ID
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.movieDetailsUri
import com.koniukhov.cinecircle.core.common.navigation.NavArgs.tvSeriesDetailsUri
import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.ui.adapter.MediaAdapter
import com.koniukhov.cinecircle.core.ui.utils.GridSpacingItemDecoration
import com.koniukhov.cinecircle.feature.collections.databinding.BottomSheetCollectionContentBinding
import com.koniukhov.cinecircle.feature.lists.viewmodel.CollectionContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionContentBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetCollectionContentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CollectionContentViewModel by viewModels()
    private lateinit var adapter: MediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCollectionContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (collectionId, collectionName) = setupArguments() ?: return

        setupTitle(collectionName)
        setupRecyclerView()
        observeCollectionContent(collectionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupTitle(collectionName: String) {
        binding.collectionTitle.text = collectionName
    }

    private fun setupRecyclerView() {
        adapter = MediaAdapter { mediaId, mediaType ->
            navigateToDetails(mediaId, mediaType)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), NUMBER_OF_ELEMENTS_IN_ROW)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(NUMBER_OF_ELEMENTS_IN_ROW, spacing, true))
        binding.recyclerView.adapter = adapter
    }

    private fun observeCollectionContent(collectionId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getCollectionContent(collectionId).collectLatest { items ->
                    binding.loadingStateLayout.visibility = View.GONE

                    if (items.isEmpty()) {
                        binding.emptyStateLayout.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.emptyStateLayout.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        adapter.setMediaItems(items)
                    }
                }
            }
        }
    }

    private fun navigateToDetails(mediaId: Int, mediaType: MediaType) {
        val uri = when (mediaType) {
            MediaType.MOVIE -> movieDetailsUri(mediaId)
            MediaType.TV_SERIES -> tvSeriesDetailsUri(mediaId)
        }

        val request = NavDeepLinkRequest.Builder
            .fromUri(uri)
            .build()
        findNavController().navigate(request)
        dismiss()
    }

    private fun setupArguments(): Pair<Long, String>? {
        val collectionId = arguments?.getLong(ARG_COLLECTION_ID) ?:  INVALID_ID.toLong()
        val collectionName = arguments?.getString(ARG_COLLECTION_NAME) ?: "Collection Items"
        return Pair(collectionId, collectionName)
    }

    companion object {
        private const val ARG_COLLECTION_ID = "collection_id"
        private const val ARG_COLLECTION_NAME = "collection_name"
        private const val NUMBER_OF_ELEMENTS_IN_ROW = 2

        fun newInstance(collectionId: Long, collectionName: String): CollectionContentBottomSheetFragment {
            val fragment = CollectionContentBottomSheetFragment()
            val args = Bundle()
            args.putLong(ARG_COLLECTION_ID, collectionId)
            args.putString(ARG_COLLECTION_NAME, collectionName)
            fragment.arguments = args
            return fragment
        }
    }
}