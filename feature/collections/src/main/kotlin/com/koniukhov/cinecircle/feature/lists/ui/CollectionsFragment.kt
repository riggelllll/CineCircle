package com.koniukhov.cinecircle.feature.lists.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.koniukhov.cinecircle.core.database.model.MediaListWithCount
import com.koniukhov.cinecircle.core.ui.base.BaseFragment
import com.koniukhov.cinecircle.feature.collections.R
import com.koniukhov.cinecircle.feature.collections.databinding.CollectionsFragmentBinding
import com.koniukhov.cinecircle.feature.collections.databinding.DialogCreateCollectionBinding
import com.koniukhov.cinecircle.feature.lists.adapter.CollectionsAdapter
import com.koniukhov.cinecircle.feature.lists.viewmodel.CollectionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CollectionsFragment : BaseFragment<CollectionsFragmentBinding, CollectionsViewModel>() {

    override val viewModel: CollectionsViewModel by viewModels()

    private lateinit var collectionsAdapter: CollectionsAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): CollectionsFragmentBinding {
        return CollectionsFragmentBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupRecyclerView()
        setupFab()
    }

    override fun observeViewModel() {
        launchWhenStarted {
            observeCollections()
        }
    }

    private suspend fun observeCollections() {
        viewModel.collections.collectLatest { collections ->
            collectionsAdapter.submitList(collections)
        }
    }

    private fun setupRecyclerView() {
        collectionsAdapter = CollectionsAdapter { collection ->
            onCollectionClick(collection)
        }

        binding.recyclerCollections.apply {
            adapter = collectionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val swipeCallback = SwipeToDeleteCallback(requireContext()) { position ->
            val collection = collectionsAdapter.currentList[position]
            showDeleteConfirmationDialog(collection)
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerCollections)
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            showCreateCollectionDialog()
        }
    }

    private fun showCreateCollectionDialog() {
        val dialogBinding = DialogCreateCollectionBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.create, null)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()

        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val collectionName = dialogBinding.editTextCollectionName.text?.toString()?.trim()
            if (collectionName.isNullOrEmpty()) {
                dialogBinding.textInputLayout.error = getString(R.string.collection_name_error)
            } else {
                dialogBinding.textInputLayout.error = null
                viewModel.createCollection(collectionName)
                dialog.dismiss()
            }
        }

        dialogBinding.editTextCollectionName.requestFocus()
    }

    private fun showDeleteConfirmationDialog(collection: MediaListWithCount) {
        if (collection.isDefault) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_collection_title)
                .setMessage(R.string.cannot_delete_default_collection)
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .show()
            return
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_collection_title)
            .setMessage(getString(R.string.delete_collection_message, collection.name))
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteCollection(collection.id)
            }
            .setNegativeButton(R.string.cancel, null)
            .setCancelable(false)
            .show()
    }

    private fun onCollectionClick(collection: MediaListWithCount) {
        val bottomSheet = CollectionContentBottomSheetFragment.newInstance(
            collectionId = collection.id,
            collectionName = collection.name
        )
        bottomSheet.show(parentFragmentManager, TAG)
    }

    override fun onDestroyView() {
        binding.recyclerCollections.adapter = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "CollectionContentBottomSheet"
    }
}