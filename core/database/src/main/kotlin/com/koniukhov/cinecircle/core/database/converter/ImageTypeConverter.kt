package com.koniukhov.cinecircle.core.database.converter

import androidx.room.TypeConverter
import com.koniukhov.cinecircle.core.database.entity.ImageType

class ImageTypeConverter {
    @TypeConverter
    fun fromImageType(value: ImageType): String = value.name

    @TypeConverter
    fun toImageType(value: String): ImageType = ImageType.valueOf(value)
}