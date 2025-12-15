package com.koniukhov.cinecircle.core.design.util

import com.koniukhov.cinecircle.core.design.R
import com.koniukhov.cinecircle.core.domain.model.Genre
import org.junit.Assert.*
import org.junit.Test

class GenreDrawableMapperTest {

    @Test
    fun `getMoviesGenreUiList should map Action genre correctly`() {
        val genres = listOf(Genre(id = 28, name = "Action"))

        val result = getMoviesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(28, result[0].id)
        assertEquals("Action", result[0].name)
        assertEquals(R.drawable.movie_genre_action_poster, result[0].imageResId)
    }

    @Test
    fun `getMoviesGenreUiList should map Adventure genre correctly`() {
        val genres = listOf(Genre(id = 12, name = "Adventure"))

        val result = getMoviesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(12, result[0].id)
        assertEquals("Adventure", result[0].name)
        assertEquals(R.drawable.movie_genre_adventure_poster, result[0].imageResId)
    }

    @Test
    fun `getMoviesGenreUiList should map Comedy genre correctly`() {
        val genres = listOf(Genre(id = 35, name = "Comedy"))

        val result = getMoviesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.movie_genre_comedy_poster, result[0].imageResId)
    }

    @Test
    fun `getMoviesGenreUiList should map Drama genre correctly`() {
        val genres = listOf(Genre(id = 18, name = "Drama"))

        val result = getMoviesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.movie_genre_drama_poster, result[0].imageResId)
    }

    @Test
    fun `getMoviesGenreUiList should map Horror genre correctly`() {
        val genres = listOf(Genre(id = 27, name = "Horror"))

        val result = getMoviesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.movie_genre_horror_poster, result[0].imageResId)
    }

    @Test
    fun `getMoviesGenreUiList should map Science Fiction genre correctly`() {
        val genres = listOf(Genre(id = 878, name = "Science Fiction"))

        val result = getMoviesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.movie_genre_science_fiction_poster, result[0].imageResId)
    }

    @Test
    fun `getMoviesGenreUiList should filter out unmapped genres`() {
        val genres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 99999, name = "Unknown Genre"),
            Genre(id = 35, name = "Comedy")
        )

        val result = getMoviesGenreUiList(genres)

        assertEquals(2, result.size)
        assertEquals(28, result[0].id)
        assertEquals(35, result[1].id)
    }

    @Test
    fun `getMoviesGenreUiList should return empty list for empty input`() {
        val result = getMoviesGenreUiList(emptyList())

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMoviesGenreUiList should return empty list when no genres match`() {
        val genres = listOf(
            Genre(id = 11111, name = "Unknown 1"),
            Genre(id = 22222, name = "Unknown 2")
        )

        val result = getMoviesGenreUiList(genres)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMoviesGenreUiList should map multiple genres in order`() {
        val genres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 12, name = "Adventure"),
            Genre(id = 35, name = "Comedy")
        )

        val result = getMoviesGenreUiList(genres)

        assertEquals(3, result.size)
        assertEquals(28, result[0].id)
        assertEquals(12, result[1].id)
        assertEquals(35, result[2].id)
    }

    @Test
    fun `getTvSeriesGenreUiList should map Action Adventure genre correctly`() {
        val genres = listOf(Genre(id = 10759, name = "Action & Adventure"))

        val result = getTvSeriesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(10759, result[0].id)
        assertEquals("Action & Adventure", result[0].name)
        assertEquals(R.drawable.tv_series_genre_action_adventure_poster, result[0].imageResId)
    }

    @Test
    fun `getTvSeriesGenreUiList should map Animation genre correctly`() {
        val genres = listOf(Genre(id = 16, name = "Animation"))

        val result = getTvSeriesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.tv_series_genre_animation_poster, result[0].imageResId)
    }

    @Test
    fun `getTvSeriesGenreUiList should map Comedy genre correctly`() {
        val genres = listOf(Genre(id = 35, name = "Comedy"))

        val result = getTvSeriesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.tv_series_genre_comedy_poster, result[0].imageResId)
    }

    @Test
    fun `getTvSeriesGenreUiList should map Drama genre correctly`() {
        val genres = listOf(Genre(id = 18, name = "Drama"))

        val result = getTvSeriesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.tv_series_genre_drama_poster, result[0].imageResId)
    }

    @Test
    fun `getTvSeriesGenreUiList should map Kids genre correctly`() {
        val genres = listOf(Genre(id = 10762, name = "Kids"))

        val result = getTvSeriesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.tv_series_genre_kids_poster, result[0].imageResId)
    }

    @Test
    fun `getTvSeriesGenreUiList should map Sci-Fi Fantasy genre correctly`() {
        val genres = listOf(Genre(id = 10765, name = "Sci-Fi & Fantasy"))

        val result = getTvSeriesGenreUiList(genres)

        assertEquals(1, result.size)
        assertEquals(R.drawable.tv_series_genre_sci_fi_fantasy_poster, result[0].imageResId)
    }

    @Test
    fun `getTvSeriesGenreUiList should filter out unmapped genres`() {
        val genres = listOf(
            Genre(id = 10759, name = "Action & Adventure"),
            Genre(id = 99999, name = "Unknown Genre"),
            Genre(id = 16, name = "Animation")
        )

        val result = getTvSeriesGenreUiList(genres)

        assertEquals(2, result.size)
        assertEquals(10759, result[0].id)
        assertEquals(16, result[1].id)
    }

    @Test
    fun `getTvSeriesGenreUiList should return empty list for empty input`() {
        val result = getTvSeriesGenreUiList(emptyList())

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesGenreUiList should return empty list when no genres match`() {
        val genres = listOf(
            Genre(id = 11111, name = "Unknown 1"),
            Genre(id = 22222, name = "Unknown 2")
        )

        val result = getTvSeriesGenreUiList(genres)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getTvSeriesGenreUiList should map multiple genres in order`() {
        val genres = listOf(
            Genre(id = 10759, name = "Action & Adventure"),
            Genre(id = 18, name = "Drama"),
            Genre(id = 10765, name = "Sci-Fi & Fantasy")
        )

        val result = getTvSeriesGenreUiList(genres)

        assertEquals(3, result.size)
        assertEquals(10759, result[0].id)
        assertEquals(18, result[1].id)
        assertEquals(10765, result[2].id)
    }

    @Test
    fun `Animation genre ID 16 exists in both movies and TV series maps`() {
        val genre = Genre(id = 16, name = "Animation")

        val movieResult = getMoviesGenreUiList(listOf(genre))
        val tvResult = getTvSeriesGenreUiList(listOf(genre))

        assertEquals(1, movieResult.size)
        assertEquals(1, tvResult.size)
        assertEquals(R.drawable.movie_genre_animation_poster, movieResult[0].imageResId)
        assertEquals(R.drawable.tv_series_genre_animation_poster, tvResult[0].imageResId)
    }

    @Test
    fun `Comedy genre ID 35 exists in both movies and TV series maps`() {
        val genre = Genre(id = 35, name = "Comedy")

        val movieResult = getMoviesGenreUiList(listOf(genre))
        val tvResult = getTvSeriesGenreUiList(listOf(genre))

        assertEquals(1, movieResult.size)
        assertEquals(1, tvResult.size)
    }

    @Test
    fun `Drama genre ID 18 exists in both movies and TV series maps`() {
        val genre = Genre(id = 18, name = "Drama")

        val movieResult = getMoviesGenreUiList(listOf(genre))
        val tvResult = getTvSeriesGenreUiList(listOf(genre))

        assertEquals(1, movieResult.size)
        assertEquals(1, tvResult.size)
    }

    @Test
    fun `moviesGenreDrawableMap should contain 19 entries`() {
        assertEquals(19, moviesGenreDrawableMap.size)
    }

    @Test
    fun `tvSeriesGenreDrawableMap should contain 16 entries`() {
        assertEquals(16, tvSeriesGenreDrawableMap.size)
    }

    @Test
    fun `all movie genre IDs should be positive integers`() {
        moviesGenreDrawableMap.keys.forEach { id ->
            assertTrue("Genre ID $id should be positive", id > 0)
        }
    }

    @Test
    fun `all TV series genre IDs should be positive integers`() {
        tvSeriesGenreDrawableMap.keys.forEach { id ->
            assertTrue("Genre ID $id should be positive", id > 0)
        }
    }
}