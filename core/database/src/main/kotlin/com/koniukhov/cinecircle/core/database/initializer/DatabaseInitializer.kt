package com.koniukhov.cinecircle.core.database.initializer

import android.content.Context
import com.koniukhov.cinecircle.core.database.CineCircleDatabase
import com.koniukhov.cinecircle.core.database.entity.MediaListEntity
import com.koniukhov.cinecircle.core.database.R

class DatabaseInitializer(
    private val context: Context,
    private val database: CineCircleDatabase
) {

    suspend fun initializeDatabase() {
        val mediaListDao = database.mediaListDao()

        val defaultList = mediaListDao.getDefaultList()

        if (defaultList == null) {
            val favoritesListName = context.getString(R.string.favorites_list_name)
            val favoritesList = MediaListEntity(
                name = favoritesListName,
                isDefault = true
            )
            mediaListDao.insertList(favoritesList)
        }
    }
}