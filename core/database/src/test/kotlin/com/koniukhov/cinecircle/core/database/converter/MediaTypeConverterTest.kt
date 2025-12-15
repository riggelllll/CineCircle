package com.koniukhov.cinecircle.core.database.converter

import com.koniukhov.cinecircle.core.common.MediaType
import org.junit.Assert.*
import org.junit.Test

class MediaTypeConverterTest {

    private val converter = MediaTypeConverter()

    @Test
    fun `fromMediaType should convert MOVIE to string`() {
        val result = converter.fromMediaType(MediaType.MOVIE)

        assertEquals("MOVIE", result)
    }

    @Test
    fun `fromMediaType should convert TV_SERIES to string`() {
        val result = converter.fromMediaType(MediaType.TV_SERIES)

        assertEquals("TV_SERIES", result)
    }

    @Test
    fun `fromMediaType should handle null value`() {
        val result = converter.fromMediaType(null)

        assertNull(result)
    }

    @Test
    fun `toMediaType should convert MOVIE string to MediaType`() {
        val result = converter.toMediaType("MOVIE")

        assertEquals(MediaType.MOVIE, result)
    }

    @Test
    fun `toMediaType should convert TV_SERIES string to MediaType`() {
        val result = converter.toMediaType("TV_SERIES")

        assertEquals(MediaType.TV_SERIES, result)
    }

    @Test
    fun `toMediaType should handle null value`() {
        val result = converter.toMediaType(null)

        assertNull(result)
    }

    @Test
    fun `roundtrip conversion should preserve MediaType MOVIE`() {
        val original = MediaType.MOVIE
        val converted = converter.fromMediaType(original)
        val restored = converter.toMediaType(converted)

        assertEquals(original, restored)
    }

    @Test
    fun `roundtrip conversion should preserve MediaType TV_SERIES`() {
        val original = MediaType.TV_SERIES
        val converted = converter.fromMediaType(original)
        val restored = converter.toMediaType(converted)

        assertEquals(original, restored)
    }

    @Test
    fun `roundtrip conversion should preserve null`() {
        val converted = converter.fromMediaType(null)
        val restored = converter.toMediaType(converted)

        assertNull(restored)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toMediaType should throw exception for invalid string`() {
        converter.toMediaType("INVALID_TYPE")
    }

    @Test
    fun `toMediaType should be case sensitive`() {
        try {
            converter.toMediaType("movie")
            fail("Should have thrown IllegalArgumentException")
        } catch (_: IllegalArgumentException) {
            assertTrue(true)
        }
    }
}