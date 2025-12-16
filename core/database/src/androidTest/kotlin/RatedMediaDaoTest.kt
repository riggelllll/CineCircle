package com.koniukhov.cinecircle.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecircle.core.database.CineCircleDatabase
import com.koniukhov.cinecircle.core.database.entity.RatedMediaEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RatedMediaDaoTest {

    private lateinit var database: CineCircleDatabase
    private lateinit var ratedMediaDao: RatedMediaDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            CineCircleDatabase::class.java
        ).allowMainThreadQueries().build()

        ratedMediaDao = database.ratedMediaDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertRatedMedia_shouldInsertMediaWithRating() = runTest {
        val ratedMedia = RatedMediaEntity(mediaId = 100L, rating = 4.5f)

        ratedMediaDao.insertRatedMedia(ratedMedia)

        val result = ratedMediaDao.getRatedMedia(100L)
        assertNotNull(result)
        assertEquals(100L, result?.mediaId)
        assertEquals(4.5f, result?.rating)
    }

    @Test
    fun insertRatedMedia_shouldReplaceExistingRating() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 100L, rating = 3.0f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 100L, rating = 4.0f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)

        val result = ratedMediaDao.getRatedMedia(100L)
        assertEquals(4.0f, result?.rating)
    }

    @Test
    fun getRatedMedia_shouldReturnNullForNonExistentMedia() = runTest {
        val result = ratedMediaDao.getRatedMedia(999L)

        assertNull(result)
    }

    @Test
    fun deleteRatedMedia_shouldRemoveMediaFromDatabase() = runTest {
        val ratedMedia = RatedMediaEntity(mediaId = 100L, rating = 3.5f)
        ratedMediaDao.insertRatedMedia(ratedMedia)

        ratedMediaDao.deleteRatedMedia(ratedMedia)

        val result = ratedMediaDao.getRatedMedia(100L)
        assertNull(result)
    }

    @Test
    fun getAllRatedMedias_shouldReturnAllRatedMedias() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 100L, rating = 3.5f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 200L, rating = 5.0f)
        val ratedMedia3 = RatedMediaEntity(mediaId = 300L, rating = 4.5f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)
        ratedMediaDao.insertRatedMedia(ratedMedia3)

        val result = ratedMediaDao.getAllRatedMedias()

        assertEquals(3, result.size)
    }

    @Test
    fun getAllRatedMedias_shouldReturnEmptyListWhenDatabaseIsEmpty() = runTest {
        val result = ratedMediaDao.getAllRatedMedias()

        assertTrue(result.isEmpty())
    }

    @Test
    fun clearAll_shouldRemoveAllRatedMedias() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 100L, rating = 4.5f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 200L, rating = 5.0f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)

        ratedMediaDao.clearAll()

        val result = ratedMediaDao.getAllRatedMedias()
        assertTrue(result.isEmpty())
    }

    @Test
    fun insertRatedMedia_shouldHandleMultipleDifferentMedias() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 100L, rating = 3.5f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 200L, rating = 4.0f)
        val ratedMedia3 = RatedMediaEntity(mediaId = 300L, rating = 4.5f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)
        ratedMediaDao.insertRatedMedia(ratedMedia3)

        val result1 = ratedMediaDao.getRatedMedia(100L)
        val result2 = ratedMediaDao.getRatedMedia(200L)
        val result3 = ratedMediaDao.getRatedMedia(300L)

        assertEquals(3.5f, result1?.rating)
        assertEquals(4.0f, result2?.rating)
        assertEquals(4.5f, result3?.rating)
    }

    @Test
    fun getRatedMedia_shouldReturnCorrectMediaAmongMultipleEntries() = runTest {
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 3.5f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 200L, rating = 5.0f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 300L, rating = 4.5f))

        val result = ratedMediaDao.getRatedMedia(200L)

        assertNotNull(result)
        assertEquals(200L, result?.mediaId)
        assertEquals(5.0f, result?.rating)
    }

    @Test
    fun insertRatedMedia_shouldHandleEdgeCaseRatings() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 100L, rating = 0.0f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 200L, rating = 4.0f)
        val ratedMedia3 = RatedMediaEntity(mediaId = 300L, rating = 0.5f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)
        ratedMediaDao.insertRatedMedia(ratedMedia3)

        assertEquals(0.0f, ratedMediaDao.getRatedMedia(100L)?.rating)
        assertEquals(4.0f, ratedMediaDao.getRatedMedia(200L)?.rating)
        assertEquals(0.5f, ratedMediaDao.getRatedMedia(300L)?.rating)
    }

    @Test
    fun deleteRatedMedia_shouldNotAffectOtherEntries() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 100L, rating = 3.5f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 200L, rating = 4.0f)
        val ratedMedia3 = RatedMediaEntity(mediaId = 300L, rating = 4.5f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)
        ratedMediaDao.insertRatedMedia(ratedMedia3)

        ratedMediaDao.deleteRatedMedia(ratedMedia2)

        val all = ratedMediaDao.getAllRatedMedias()
        assertEquals(2, all.size)
        assertNull(ratedMediaDao.getRatedMedia(200L))
        assertNotNull(ratedMediaDao.getRatedMedia(100L))
        assertNotNull(ratedMediaDao.getRatedMedia(300L))
    }

    @Test
    fun getAllRatedMedias_shouldReturnCorrectOrder() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 300L, rating = 3.5f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 100L, rating = 4.5f)
        val ratedMedia3 = RatedMediaEntity(mediaId = 200L, rating = 4.0f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)
        ratedMediaDao.insertRatedMedia(ratedMedia3)

        val result = ratedMediaDao.getAllRatedMedias()

        assertEquals(3, result.size)
        assertTrue(result.any { it.mediaId == 100L })
        assertTrue(result.any { it.mediaId == 200L })
        assertTrue(result.any { it.mediaId == 300L })
    }

    @Test
    fun clearAll_shouldWorkOnEmptyDatabase() = runTest {
        ratedMediaDao.clearAll()

        val result = ratedMediaDao.getAllRatedMedias()
        assertTrue(result.isEmpty())
    }

    @Test
    fun insertAndUpdateSameMediaMultipleTimes_shouldKeepLatestValue() = runTest {
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 3.0f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 5.0f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 5.0f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 4.0f))

        val result = ratedMediaDao.getRatedMedia(100L)

        assertEquals(4.0f, result?.rating)
        assertEquals(1, ratedMediaDao.getAllRatedMedias().size)
    }
}