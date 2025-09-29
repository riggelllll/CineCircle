package com.koniukhov.cinecircle.core.database.converter

import androidx.room.TypeConverter
import com.koniukhov.cinecircle.core.common.MediaType

class MediaTypeConverter {
    @TypeConverter
    fun fromMediaType(value: MediaType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toMediaType(value: String?): MediaType? {
        return value?.let { MediaType.valueOf(it) }
    }
}