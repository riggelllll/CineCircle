package com.koniukhov.cinecircle.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.koniukhov.cinecircle.core.common.MediaType
import com.koniukhov.cinecircle.core.database.CineCircleDatabase
import com.koniukhov.cinecircle.core.database.entity.MediaListEntity
import com.koniukhov.cinecircle.core.database.entity.MediaListItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class MediaListDaoTest {

    private lateinit var database: CineCircleDatabase
    private lateinit var mediaListDao: MediaListDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            CineCircleDatabase::class.java
        ).allowMainThreadQueries().build()

        mediaListDao = database.mediaListDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `insertList should insert and return list ID`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)

        val insertedId = mediaListDao.insertList(list)

        assertTrue(insertedId > 0)
    }

    @Test
    fun `getListById should return inserted list`() = runTest {
        val list = MediaListEntity(name = "Test List", isDefault = false)
        val insertedId = mediaListDao.insertList(list)

        val result = mediaListDao.getListById(insertedId)

        assertNotNull(result)
        assertEquals("Test List", result?.name)
        assertEquals(false, result?.isDefault)
    }

    @Test
    fun `getListById should return null for non-existent ID`() = runTest {
        val result = mediaListDao.getListById(999L)

        assertNull(result)
    }

    @Test
    fun `getAllLists should return all lists ordered by isDefault DESC`() = runTest {
        val regularList1 = MediaListEntity(name = "Regular 1", isDefault = false)
        val regularList2 = MediaListEntity(name = "Regular 2", isDefault = false)
        val defaultList = MediaListEntity(name = "Default", isDefault = true)

        mediaListDao.insertList(regularList1)
        mediaListDao.insertList(regularList2)
        mediaListDao.insertList(defaultList)

        val result = mediaListDao.getAllLists().first()

        assertEquals(3, result.size)
        assertTrue(result[0].isDefault)
        assertEquals("Default", result[0].name)
    }

    @Test
    fun `getDefaultList should return default list`() = runTest {
        val regularList = MediaListEntity(name = "Regular", isDefault = false)
        val defaultList = MediaListEntity(name = "Default", isDefault = true)

        mediaListDao.insertList(regularList)
        mediaListDao.insertList(defaultList)

        val result = mediaListDao.getDefaultList()

        assertNotNull(result)
        assertEquals("Default", result?.name)
        assertTrue(result?.isDefault == true)
    }

    @Test
    fun `getDefaultList should return null when no default list exists`() = runTest {
        val regularList = MediaListEntity(name = "Regular", isDefault = false)
        mediaListDao.insertList(regularList)

        val result = mediaListDao.getDefaultList()

        assertNull(result)
    }

    @Test
    fun `updateList should update list name`() = runTest {
        val list = MediaListEntity(name = "Original Name", isDefault = false)
        val insertedId = mediaListDao.insertList(list)

        val updatedList = list.copy(id = insertedId, name = "Updated Name")
        mediaListDao.updateList(updatedList)

        val result = mediaListDao.getListById(insertedId)
        assertEquals("Updated Name", result?.name)
    }

    @Test
    fun `deleteList should delete non-default list`() = runTest {
        val list = MediaListEntity(name = "To Delete", isDefault = false)
        val insertedId = mediaListDao.insertList(list)

        mediaListDao.deleteList(insertedId)

        val result = mediaListDao.getListById(insertedId)
        assertNull(result)
    }

    @Test
    fun `deleteList should not delete default list`() = runTest {
        val defaultList = MediaListEntity(name = "Default", isDefault = true)
        val insertedId = mediaListDao.insertList(defaultList)

        mediaListDao.deleteList(insertedId)

        val result = mediaListDao.getListById(insertedId)
        assertNotNull(result)
    }

    @Test
    fun `addMediaToList should add media item`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        val item = MediaListItemEntity(
            listId = listId,
            mediaId = 100,
            mediaType = MediaType.MOVIE
        )
        val itemId = mediaListDao.addMediaToList(item)

        assertTrue(itemId > 0)
    }

    @Test
    fun `isMediaInList should return 1 when media is in list`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)
        val item = MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE)
        mediaListDao.addMediaToList(item)

        val result = mediaListDao.isMediaInList(listId, 100, MediaType.MOVIE)

        assertEquals(1, result)
    }

    @Test
    fun `isMediaInList should return 0 when media is not in list`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        val result = mediaListDao.isMediaInList(listId, 100, MediaType.MOVIE)

        assertEquals(0, result)
    }

    @Test
    fun `getMediaItemsInList should return items in correct order`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        val item1 = MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE)
        val item2 = MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.TV_SERIES)
        val item3 = MediaListItemEntity(listId = listId, mediaId = 300, mediaType = MediaType.MOVIE)

        mediaListDao.addMediaToList(item1)
        mediaListDao.addMediaToList(item2)
        mediaListDao.addMediaToList(item3)

        val result = mediaListDao.getMediaItemsInList(listId).first()

        assertEquals(3, result.size)
        assertEquals(300, result[0].mediaId)
        assertEquals(200, result[1].mediaId)
        assertEquals(100, result[2].mediaId)
    }

    @Test
    fun `removeMediaFromList should remove specific media`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)
        val item = MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE)
        mediaListDao.addMediaToList(item)

        mediaListDao.removeMediaFromList(listId, 100, MediaType.MOVIE)

        val result = mediaListDao.isMediaInList(listId, 100, MediaType.MOVIE)
        assertEquals(0, result)
    }

    @Test
    fun `clearList should remove all items from list`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.TV_SERIES))

        mediaListDao.clearList(listId)

        val result = mediaListDao.getMediaItemsInList(listId).first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMediaCountInList should return correct count`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 300, mediaType = MediaType.TV_SERIES))

        val result = mediaListDao.getMediaCountInList(listId)

        assertEquals(3, result)
    }

    @Test
    fun `getMediaCountInListAsFlow should emit correct count`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.MOVIE))

        val result = mediaListDao.getMediaCountInListAsFlow(listId).first()

        assertEquals(2, result)
    }

    @Test
    fun `getAllListsWithCount should return lists with item counts`() = runTest {
        val list1 = MediaListEntity(name = "List 1", isDefault = false)
        val list2 = MediaListEntity(name = "List 2", isDefault = true)
        val listId1 = mediaListDao.insertList(list1)
        val listId2 = mediaListDao.insertList(list2)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId1, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId1, mediaId = 200, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId2, mediaId = 300, mediaType = MediaType.TV_SERIES))

        val result = mediaListDao.getAllListsWithCount().first()

        assertEquals(2, result.size)
        assertEquals("List 2", result[0].mediaList.name)
        assertEquals(1, result[0].itemCount)
        assertEquals("List 1", result[1].mediaList.name)
        assertEquals(2, result[1].itemCount)
    }

    @Test
    fun `getListsContainingMedia should return correct lists`() = runTest {
        val list1 = MediaListEntity(name = "List 1", isDefault = false)
        val list2 = MediaListEntity(name = "List 2", isDefault = true)
        val list3 = MediaListEntity(name = "List 3", isDefault = false)
        val listId1 = mediaListDao.insertList(list1)
        val listId2 = mediaListDao.insertList(list2)
        val listId3 = mediaListDao.insertList(list3)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId1, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId2, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId3, mediaId = 200, mediaType = MediaType.MOVIE))

        val result = mediaListDao.getListsContainingMedia(100, MediaType.MOVIE)

        assertEquals(2, result.size)
        assertTrue(result[0].isDefault)
        assertEquals("List 2", result[0].name)
        assertEquals("List 1", result[1].name)
    }

    @Test
    fun `getMediaByTypeInList should filter by media type`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.TV_SERIES))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 300, mediaType = MediaType.MOVIE))

        val result = mediaListDao.getMediaByTypeInList(listId, MediaType.MOVIE).first()

        assertEquals(2, result.size)
        assertEquals(MediaType.MOVIE, result[0].mediaType)
        assertEquals(MediaType.MOVIE, result[1].mediaType)
    }

    @Test
    fun `addMediaToList should ignore duplicate entries`() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        val item = MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE)
        mediaListDao.addMediaToList(item)
        mediaListDao.addMediaToList(item)

        val count = mediaListDao.getMediaCountInList(listId)

        assertEquals(1, count)
    }

    @Test
    fun `cascade delete should remove items when list is deleted`() = runTest {
        val list = MediaListEntity(name = "To Delete", isDefault = false)
        val listId = mediaListDao.insertList(list)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.MOVIE))

        mediaListDao.deleteList(listId)

        val deletedList = mediaListDao.getListById(listId)
        assertNull(deletedList)

        val items = mediaListDao.getMediaItemsInList(listId).first()
        assertTrue(items.isEmpty())
    }
}