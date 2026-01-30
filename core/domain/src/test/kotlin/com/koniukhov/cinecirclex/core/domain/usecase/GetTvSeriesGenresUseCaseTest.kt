package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.Genre
import com.koniukhov.cinecirclex.core.domain.repository.GenresRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTvSeriesGenresUseCaseTest {

    private lateinit var repository: GenresRepository
    private lateinit var useCase: GetTvSeriesGenresUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesGenresUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns tv series genres from repository`() = runTest {
        val language = "en"
        val expectedGenres = listOf(
            Genre(id = 10759, name = "Action & Adventure"),
            Genre(id = 16, name = "Animation"),
            Genre(id = 35, name = "Comedy"),
            Genre(id = 80, name = "Crime"),
            Genre(id = 99, name = "Documentary"),
            Genre(id = 18, name = "Drama"),
            Genre(id = 10751, name = "Family"),
            Genre(id = 10762, name = "Kids"),
            Genre(id = 9648, name = "Mystery"),
            Genre(id = 10763, name = "News"),
            Genre(id = 10764, name = "Reality"),
            Genre(id = 10765, name = "Sci-Fi & Fantasy"),
            Genre(id = 10766, name = "Soap"),
            Genre(id = 10767, name = "Talk"),
            Genre(id = 10768, name = "War & Politics"),
            Genre(id = 37, name = "Western")
        )

        coEvery { repository.getTvSeriesGenreList(language) } returns expectedGenres

        val result = useCase(language)

        assertEquals(expectedGenres, result)
        coVerify(exactly = 1) { repository.getTvSeriesGenreList(language) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        val language = "en"

        coEvery { repository.getTvSeriesGenreList(language) } returns emptyList()

        val result = useCase(language)

        assertEquals(emptyList<Genre>(), result)
        coVerify(exactly = 1) { repository.getTvSeriesGenreList(language) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val language = "en"

        coEvery { repository.getTvSeriesGenreList(language) } throws Exception("Network error")

        useCase(language)
    }

    @Test
    fun `invoke handles single genre correctly`() = runTest {
        val language = "en"
        val expectedGenres = listOf(
            Genre(id = 18, name = "Drama")
        )

        coEvery { repository.getTvSeriesGenreList(language) } returns expectedGenres

        val result = useCase(language)

        assertEquals(expectedGenres, result)
        assertEquals(1, result.size)
        coVerify(exactly = 1) { repository.getTvSeriesGenreList(language) }
    }
}