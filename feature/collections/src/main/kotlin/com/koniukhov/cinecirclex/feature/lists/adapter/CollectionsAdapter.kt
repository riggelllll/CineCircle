package com.koniukhov.cinecirclex.feature.lists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.koniukhov.cinecirclex.feature.collections.R
import com.koniukhov.cinecirclex.feature.collections.databinding.ItemCollectionBinding
import com.koniukhov.cinecirclex.core.database.model.MediaListWithCount

class CollectionsAdapter(
    private val onCollectionClick: (MediaListWithCount) -> Unit
) : ListAdapter<MediaListWithCount, CollectionsAdapter.CollectionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = ItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CollectionViewHolder(
        private val binding: ItemCollectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(collection: MediaListWithCount) {
            binding.listName.text = collection.name

            val itemCountText = binding.root.context.resources.getQuantityString(
                R.plurals.items_count,
                collection.itemCount,
                collection.itemCount
            )
            binding.itemCount.text = itemCountText

            binding.root.setOnClickListener {
                onCollectionClick(collection)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<MediaListWithCount>() {
        override fun areItemsTheSame(oldItem: MediaListWithCount, newItem: MediaListWithCount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MediaListWithCount, newItem: MediaListWithCount): Boolean {
            return oldItem == newItem
        }
    }
}