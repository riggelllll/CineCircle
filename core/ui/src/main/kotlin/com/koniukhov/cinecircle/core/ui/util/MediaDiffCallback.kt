package com.koniukhov.cinecircle.core.ui.util

import androidx.recyclerview.widget.DiffUtil
import com.koniukhov.cinecircle.core.common.model.GenreUi
import com.koniukhov.cinecircle.core.domain.model.CastMember
import com.koniukhov.cinecircle.core.domain.model.CollectionMedia
import com.koniukhov.cinecircle.core.domain.model.CrewMember
import com.koniukhov.cinecircle.core.domain.model.Image
import com.koniukhov.cinecircle.core.domain.model.MediaItem
import com.koniukhov.cinecircle.core.domain.model.MediaReview
import com.koniukhov.cinecircle.core.domain.model.TvEpisodeDetails
import com.koniukhov.cinecircle.core.domain.model.TvSeasonDetails
import com.koniukhov.cinecircle.core.domain.model.Video

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

class CollectionMediaDiffCallback(
    private val oldList: List<CollectionMedia>,
    private val newList: List<CollectionMedia>
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

class CastMemberDiffCallback(
    private val oldList: List<CastMember>,
    private val newList: List<CastMember>
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
                oldItem.name == newItem.name &&
                oldItem.character == newItem.character &&
                oldItem.profilePath == newItem.profilePath
    }
}

class CrewMemberDiffCallback(
    private val oldList: List<CrewMember>,
    private val newList: List<CrewMember>
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
                oldItem.name == newItem.name &&
                oldItem.job == newItem.job &&
                oldItem.profilePath == newItem.profilePath
    }
}

class ImageDiffCallback(
    private val oldList: List<Image>,
    private val newList: List<Image>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].filePath == newList[newItemPosition].filePath
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.filePath == newItem.filePath &&
                oldItem.width == newItem.width &&
                oldItem.height == newItem.height &&
                oldItem.voteAverage == newItem.voteAverage
    }
}

class GenreUiDiffCallback(
    private val oldList: List<GenreUi>,
    private val newList: List<GenreUi>
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
                oldItem.name == newItem.name &&
                oldItem.imageResId == newItem.imageResId
    }
}

class VideoDiffCallback(
    private val oldList: List<Video>,
    private val newList: List<Video>
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
                oldItem.key == newItem.key &&
                oldItem.name == newItem.name &&
                oldItem.type == newItem.type
    }
}

class MediaReviewDiffCallback(
    private val oldList: List<MediaReview>,
    private val newList: List<MediaReview>
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
                oldItem.content == newItem.content &&
                oldItem.author == newItem.author &&
                oldItem.authorDetails == newItem.authorDetails
    }
}

class TvSeasonDetailsDiffCallback(
    private val oldList: List<TvSeasonDetails>,
    private val newList: List<TvSeasonDetails>
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
                oldItem.name == newItem.name &&
                oldItem.posterPath == newItem.posterPath &&
                oldItem.voteAverage == newItem.voteAverage &&
                oldItem.seasonNumber == newItem.seasonNumber
    }
}

class TvEpisodeDetailsDiffCallback(
    private val oldList: List<TvEpisodeDetails>,
    private val newList: List<TvEpisodeDetails>
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
                oldItem.name == newItem.name &&
                oldItem.episodeNumber == newItem.episodeNumber &&
                oldItem.overview == newItem.overview &&
                oldItem.stillPath == newItem.stillPath &&
                oldItem.runtime == newItem.runtime
    }
}