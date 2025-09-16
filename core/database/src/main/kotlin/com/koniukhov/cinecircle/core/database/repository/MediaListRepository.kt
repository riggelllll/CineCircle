package com.koniukhov.cinecircle.core.database.repository

import com.koniukhov.cinecircle.core.common.Constants.MediaType
import com.koniukhov.cinecircle.core.database.dao.MediaListDao
import com.koniukhov.cinecircle.core.database.entity.MediaListEntity
import com.koniukhov.cinecircle.core.database.entity.MediaListItemEntity
import com.koniukhov.cinecircle.core.database.model.MediaListWithCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaListRepository(
    private val mediaListDao: MediaListDao
) {

    fun getAllLists(): Flow<List<MediaListEntity>> = mediaListDao.getAllLists()

    suspend fun getListById(listId: Long): MediaListEntity? = mediaListDao.getListById(listId)

    suspend fun getDefaultList(): MediaListEntity? = mediaListDao.getDefaultList()

    suspend fun createList(name: String): Long {
        val list = MediaListEntity(name = name)
        return mediaListDao.insertList(list)
    }

    suspend fun updateListName(listId: Long, newName: String) {
        val list = mediaListDao.getListById(listId)
        list?.let {
            mediaListDao.updateList(it.copy(name = newName))
        }
    }

    suspend fun deleteList(listId: Long) {
        mediaListDao.deleteList(listId)
    }

    fun getMediaItemsInList(listId: Long): Flow<List<MediaListItemEntity>> =
        mediaListDao.getMediaItemsInList(listId)

    suspend fun addMovieToList(listId: Long, movieId: Int): Boolean {
        val item = MediaListItemEntity(
            listId = listId,
            mediaId = movieId,
            mediaType = MediaType.MOVIE
        )
        return mediaListDao.addMediaToList(item) > 0
    }

    suspend fun addTvSeriesToList(listId: Long, tvSeriesId: Int): Boolean {
        val item = MediaListItemEntity(
            listId = listId,
            mediaId = tvSeriesId,
            mediaType = MediaType.TV_SERIES
        )
        return mediaListDao.addMediaToList(item) > 0
    }

    suspend fun removeMovieFromList(listId: Long, movieId: Int) {
        mediaListDao.removeMediaFromList(listId, movieId, MediaType.MOVIE)
    }

    suspend fun removeTvSeriesFromList(listId: Long, tvSeriesId: Int) {
        mediaListDao.removeMediaFromList(listId, tvSeriesId, MediaType.TV_SERIES)
    }

    suspend fun isMovieInList(listId: Long, movieId: Int): Boolean {
        return mediaListDao.isMediaInList(listId, movieId, MediaType.MOVIE) > 0
    }

    suspend fun isTvSeriesInList(listId: Long, tvSeriesId: Int): Boolean {
        return mediaListDao.isMediaInList(listId, tvSeriesId, MediaType.TV_SERIES) > 0
    }

    suspend fun getListsContainingMovie(movieId: Int): List<MediaListEntity> {
        return mediaListDao.getListsContainingMedia(movieId, MediaType.MOVIE)
    }

    suspend fun getListsContainingTvSeries(tvSeriesId: Int): List<MediaListEntity> {
        return mediaListDao.getListsContainingMedia(tvSeriesId, MediaType.TV_SERIES)
    }

    fun getMoviesInList(listId: Long): Flow<List<Int>> {
        return mediaListDao.getMediaByTypeInList(listId, MediaType.MOVIE)
            .map { items -> items.map { it.mediaId } }
    }

    fun getTvSeriesInList(listId: Long): Flow<List<Int>> {
        return mediaListDao.getMediaByTypeInList(listId, MediaType.TV_SERIES)
            .map { items -> items.map { it.mediaId } }
    }

    suspend fun getMediaCountInList(listId: Long): Int {
        return mediaListDao.getMediaCountInList(listId)
    }

    fun getMediaCountInListAsFlow(listId: Long): Flow<Int> {
        return mediaListDao.getMediaCountInListAsFlow(listId)
    }

    fun getAllListsWithCount(): Flow<List<MediaListWithCount>> {
        return mediaListDao.getAllListsWithCount().map { results ->
            results.map { result ->
                MediaListWithCount(
                    id = result.mediaList.id,
                    name = result.mediaList.name,
                    itemCount = result.itemCount,
                    isDefault = result.mediaList.isDefault
                )
            }
        }
    }


    suspend fun clearList(listId: Long) {
        mediaListDao.clearList(listId)
    }
}