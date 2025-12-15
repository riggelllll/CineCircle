package com.koniukhov.cinecircle.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.koniukhov.cinecircle.core.database.CineCircleDatabase
import com.koniukhov.cinecircle.core.database.entity.RatedMediaEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
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
    fun `insertRatedMedia should insert media with rating`() = runTest {
        val ratedMedia = RatedMediaEntity(mediaId = 100L, rating = 4.5f)

        ratedMediaDao.insertRatedMedia(ratedMedia)

        val result = ratedMediaDao.getRatedMedia(100L)
        assertNotNull(result)
        assertEquals(100L, result?.mediaId)
        assertEquals(4.5f, result?.rating)
    }

    @Test
    fun `insertRatedMedia should replace existing rating`() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 100L, rating = 3.0f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 100L, rating = 4.0f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)

        val result = ratedMediaDao.getRatedMedia(100L)
        assertEquals(4.0f, result?.rating)
    }

    @Test
    fun `getRatedMedia should return null for non-existent media`() = runTest {
        val result = ratedMediaDao.getRatedMedia(999L)

        assertNull(result)
    }

    @Test
    fun `deleteRatedMedia should remove media from database`() = runTest {
        val ratedMedia = RatedMediaEntity(mediaId = 100L, rating = 3.5f)
        ratedMediaDao.insertRatedMedia(ratedMedia)

        ratedMediaDao.deleteRatedMedia(ratedMedia)

        val result = ratedMediaDao.getRatedMedia(100L)
        assertNull(result)
    }

    @Test
    fun `getAllRatedMedias should return all rated medias`() = runTest {
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
    fun `getAllRatedMedias should return empty list when database is empty`() = runTest {
        val result = ratedMediaDao.getAllRatedMedias()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `clearAll should remove all rated medias`() = runTest {
        val ratedMedia1 = RatedMediaEntity(mediaId = 100L, rating = 4.5f)
        val ratedMedia2 = RatedMediaEntity(mediaId = 200L, rating = 5.0f)

        ratedMediaDao.insertRatedMedia(ratedMedia1)
        ratedMediaDao.insertRatedMedia(ratedMedia2)

        ratedMediaDao.clearAll()

        val result = ratedMediaDao.getAllRatedMedias()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `insertRatedMedia should handle multiple different medias`() = runTest {
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
    fun `getRatedMedia should return correct media among multiple entries`() = runTest {
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 3.5f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 200L, rating = 5.0f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 300L, rating = 4.5f))

        val result = ratedMediaDao.getRatedMedia(200L)

        assertNotNull(result)
        assertEquals(200L, result?.mediaId)
        assertEquals(5.0f, result?.rating)
    }

    @Test
    fun `insertRatedMedia should handle edge case ratings`() = runTest {
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
    fun `deleteRatedMedia should not affect other entries`() = runTest {
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
    fun `getAllRatedMedias should return correct order`() = runTest {
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
    fun `clearAll should work on empty database`() = runTest {
        ratedMediaDao.clearAll()

        val result = ratedMediaDao.getAllRatedMedias()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `insert and update same media multiple times should keep latest value`() = runTest {
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 3.0f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 5.0f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 5.0f))
        ratedMediaDao.insertRatedMedia(RatedMediaEntity(mediaId = 100L, rating = 4.0f))

        val result = ratedMediaDao.getRatedMedia(100L)

        assertEquals(4.0f, result?.rating)
        assertEquals(1, ratedMediaDao.getAllRatedMedias().size)
    }
}