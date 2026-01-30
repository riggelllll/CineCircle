package com.koniukhov.cinecirclex.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.koniukhov.cinecirclex.core.common.MediaType
import com.koniukhov.cinecirclex.core.database.CineCircleDatabase
import com.koniukhov.cinecirclex.core.database.entity.MediaListEntity
import com.koniukhov.cinecirclex.core.database.entity.MediaListItemEntity
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koniukhov.cinecirclex.core.database.dao.MediaListDao

@RunWith(AndroidJUnit4::class)
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
    fun insertList_shouldInsertAndReturnListId() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)

        val insertedId = mediaListDao.insertList(list)

        assertTrue(insertedId > 0)
    }

    @Test
    fun getListById_shouldReturnInsertedList() = runTest {
        val list = MediaListEntity(name = "Test List", isDefault = false)
        val insertedId = mediaListDao.insertList(list)

        val result = mediaListDao.getListById(insertedId)

        assertNotNull(result)
        assertEquals("Test List", result?.name)
        assertEquals(false, result?.isDefault)
    }

    @Test
    fun getListById_shouldReturnNullForNonExistentId() = runTest {
        val result = mediaListDao.getListById(999L)

        assertNull(result)
    }

    @Test
    fun getAllLists_shouldReturnAllListsOrderedByIsDefaultDesc() = runTest {
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
    fun getDefaultList_shouldReturnDefaultList() = runTest {
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
    fun getDefaultList_shouldReturnNullWhenNoDefaultListExists() = runTest {
        val regularList = MediaListEntity(name = "Regular", isDefault = false)
        mediaListDao.insertList(regularList)

        val result = mediaListDao.getDefaultList()

        assertNull(result)
    }

    @Test
    fun updateList_shouldUpdateListName() = runTest {
        val list = MediaListEntity(name = "Original Name", isDefault = false)
        val insertedId = mediaListDao.insertList(list)

        val updatedList = list.copy(id = insertedId, name = "Updated Name")
        mediaListDao.updateList(updatedList)

        val result = mediaListDao.getListById(insertedId)
        assertEquals("Updated Name", result?.name)
    }

    @Test
    fun deleteList_shouldDeleteNonDefaultList() = runTest {
        val list = MediaListEntity(name = "To Delete", isDefault = false)
        val insertedId = mediaListDao.insertList(list)

        mediaListDao.deleteList(insertedId)

        val result = mediaListDao.getListById(insertedId)
        assertNull(result)
    }

    @Test
    fun deleteList_shouldNotDeleteDefaultList() = runTest {
        val defaultList = MediaListEntity(name = "Default", isDefault = true)
        val insertedId = mediaListDao.insertList(defaultList)

        mediaListDao.deleteList(insertedId)

        val result = mediaListDao.getListById(insertedId)
        assertNotNull(result)
    }

    @Test
    fun addMediaToList_shouldAddMediaItem() = runTest {
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
    fun isMediaInList_shouldReturn1WhenMediaIsInList() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)
        val item = MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE)
        mediaListDao.addMediaToList(item)

        val result = mediaListDao.isMediaInList(listId, 100, MediaType.MOVIE)

        assertEquals(1, result)
    }

    @Test
    fun isMediaInList_shouldReturn0WhenMediaIsNotInList() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        val result = mediaListDao.isMediaInList(listId, 100, MediaType.MOVIE)

        assertEquals(0, result)
    }

    @Test
    fun getMediaItemsInList_shouldReturnItemsInCorrectOrder() = runTest {
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
    fun removeMediaFromList_shouldRemoveSpecificMedia() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)
        val item = MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE)
        mediaListDao.addMediaToList(item)

        mediaListDao.removeMediaFromList(listId, 100, MediaType.MOVIE)

        val result = mediaListDao.isMediaInList(listId, 100, MediaType.MOVIE)
        assertEquals(0, result)
    }

    @Test
    fun clearList_shouldRemoveAllItemsFromList() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.TV_SERIES))

        mediaListDao.clearList(listId)

        val result = mediaListDao.getMediaItemsInList(listId).first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getMediaCountInList_shouldReturnCorrectCount() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 300, mediaType = MediaType.TV_SERIES))

        val result = mediaListDao.getMediaCountInList(listId)

        assertEquals(3, result)
    }

    @Test
    fun getMediaCountInListAsFlow_shouldEmitCorrectCount() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE))
        mediaListDao.addMediaToList(MediaListItemEntity(listId = listId, mediaId = 200, mediaType = MediaType.MOVIE))

        val result = mediaListDao.getMediaCountInListAsFlow(listId).first()

        assertEquals(2, result)
    }

    @Test
    fun getAllListsWithCount_shouldReturnListsWithItemCounts() = runTest {
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
    fun getListsContainingMedia_shouldReturnCorrectLists() = runTest {
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
    fun getMediaByTypeInList_shouldFilterByMediaType() = runTest {
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
    fun addMediaToList_shouldIgnoreDuplicateEntries() = runTest {
        val list = MediaListEntity(name = "My List", isDefault = false)
        val listId = mediaListDao.insertList(list)

        val item = MediaListItemEntity(listId = listId, mediaId = 100, mediaType = MediaType.MOVIE)
        mediaListDao.addMediaToList(item)
        mediaListDao.addMediaToList(item)

        val count = mediaListDao.getMediaCountInList(listId)

        assertEquals(1, count)
    }

    @Test
    fun cascadeDelete_shouldRemoveItemsWhenListIsDeleted() = runTest {
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