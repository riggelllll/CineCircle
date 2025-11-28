package com.koniukhov.cinecircle.core.ui.util

import androidx.recyclerview.widget.DiffUtil
import com.koniukhov.cinecircle.core.domain.model.MediaItem

class MediaDiffCallback(
    private val oldList: List<MediaItem>,
    private val newList: List<MediaItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id &&
                oldItem.title == newItem.title &&
                oldItem.posterPath == newItem.posterPath &&
                oldItem.voteAverage == newItem.voteAverage
    }
}

object MediaItemCallback : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.title == newItem.title &&
                oldItem.posterPath == newItem.posterPath &&
                oldItem.voteAverage == newItem.voteAverage
    }
}