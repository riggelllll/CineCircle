package com.koniukhov.cinecircle.core.database.dao

import androidx.room.*
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.database.entity.GenreEntity
import com.koniukhov.cinecircle.core.database.entity.MediaListEntity
import com.koniukhov.cinecircle.core.database.entity.MediaListItemEntity
import com.koniukhov.cinecircle.core.database.entity.MediaListWithCountResult
import com.koniukhov.cinecircle.core.database.entity.MovieDetailsEntity
import com.koniukhov.cinecircle.core.database.entity.MovieWithGenres
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaListDao {

    @Query("SELECT * FROM media_lists ORDER BY isDefault DESC")
    fun getAllLists(): Flow<List<MediaListEntity>>

    @Query("SELECT * FROM media_lists WHERE id = :listId")
    suspend fun getListById(listId: Long): MediaListEntity?

    @Query("SELECT * FROM media_lists WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultList(): MediaListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: MediaListEntity): Long

    @Update
    suspend fun updateList(list: MediaListEntity)

    @Query("DELETE FROM media_lists WHERE id = :listId AND isDefault = 0")
    suspend fun deleteList(listId: Long)

    @Query("SELECT * FROM media_list_items WHERE listId = :listId ORDER BY id DESC")
    fun getMediaItemsInList(listId: Long): Flow<List<MediaListItemEntity>>

    @Query("SELECT COUNT(*) FROM media_list_items WHERE listId = :listId AND mediaId = :mediaId AND mediaType = :mediaType")
    suspend fun isMediaInList(listId: Long, mediaId: Int, mediaType: MediaType): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMediaToList(item: MediaListItemEntity): Long

    @Query("DELETE FROM media_list_items WHERE listId = :listId AND mediaId = :mediaId AND mediaType = :mediaType")
    suspend fun removeMediaFromList(listId: Long, mediaId: Int, mediaType: MediaType)

    @Query("DELETE FROM media_list_items WHERE listId = :listId")
    suspend fun clearList(listId: Long)

    @Query("SELECT COUNT(*) FROM media_list_items WHERE listId = :listId")
    suspend fun getMediaCountInList(listId: Long): Int

    @Query("SELECT COUNT(*) FROM media_list_items WHERE listId = :listId")
    fun getMediaCountInListAsFlow(listId: Long): Flow<Int>

    @Query("""
        SELECT ml.*, COUNT(mli.id) as itemCount 
        FROM media_lists ml 
        LEFT JOIN media_list_items mli ON ml.id = mli.listId 
        GROUP BY ml.id 
        ORDER BY ml.isDefault DESC
    """)
    fun getAllListsWithCount(): Flow<List<MediaListWithCountResult>>

    @Query("""
        SELECT ml.* FROM media_lists ml 
        INNER JOIN media_list_items mli ON ml.id = mli.listId 
        WHERE mli.mediaId = :mediaId AND mli.mediaType = :mediaType
        ORDER BY ml.isDefault DESC
    """)
    suspend fun getListsContainingMedia(mediaId: Int, mediaType: MediaType): List<MediaListEntity>

    @Query("SELECT * FROM media_list_items WHERE listId = :listId AND mediaType = :mediaType ORDER BY id DESC")
    fun getMediaByTypeInList(listId: Long, mediaType: MediaType): Flow<List<MediaListItemEntity>>

    @Transaction
    suspend fun insertMovieWithGenres(movieWithGenres: MovieWithGenres) {
        insertMovie(movieWithGenres.movie)
        insertGenres(movieWithGenres.genres)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Transaction
    suspend fun deleteMovieWithGenres(mediaId: Int) {
        deleteMovieByMediaId(mediaId)
        deleteGenresByMediaId(mediaId)
    }

    @Query("DELETE FROM movie_details WHERE mediaId = :mediaId")
    suspend fun deleteMovieByMediaId(mediaId: Int)

    @Query("DELETE FROM genres WHERE mediaId = :mediaId")
    suspend fun deleteGenresByMediaId(mediaId: Int)

}