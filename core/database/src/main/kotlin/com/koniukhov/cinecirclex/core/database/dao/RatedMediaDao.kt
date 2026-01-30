package com.koniukhov.cinecirclex.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.koniukhov.cinecirclex.core.database.entity.RatedMediaEntity

@Dao
interface RatedMediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRatedMedia(ratedMediaEntity: RatedMediaEntity)

    @Delete
    suspend fun deleteRatedMedia(ratedMediaEntity: RatedMediaEntity)

    @Query("DELETE FROM rated_medias")
    suspend fun clearAll()

    @Query("SELECT * FROM rated_medias")
    suspend fun getAllRatedMedias(): List<RatedMediaEntity>

    @Query("SELECT * FROM rated_medias WHERE mediaId = :mediaId LIMIT 1")
    suspend fun getRatedMedia(mediaId: Long): RatedMediaEntity?
}