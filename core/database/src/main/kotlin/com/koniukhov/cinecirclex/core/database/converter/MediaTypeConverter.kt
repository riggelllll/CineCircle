package com.koniukhov.cinecirclex.core.database.converter

import androidx.room.TypeConverter
import com.koniukhov.cinecirclex.core.common.MediaType

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