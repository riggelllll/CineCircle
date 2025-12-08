package com.koniukhov.cinecircle.core.domain.usecase

import com.koniukhov.cinecircle.core.domain.model.CastMember
import com.koniukhov.cinecircle.core.domain.model.CrewMember
import com.koniukhov.cinecircle.core.domain.model.MediaCredits
import com.koniukhov.cinecircle.core.domain.repository.CreditsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMovieCreditsUseCaseTest {

    private lateinit var repository: CreditsRepository

    private lateinit var useCase: GetMovieCreditsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieCreditsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke calls repository with correct parameters and returns result`() = runTest {
        val movieId = 123
        val language = "en"
        val expectedCredits = MediaCredits(
            cast = listOf(
                CastMember(
                    adult = false,
                    gender = 2,
                    id = 1,
                    knownForDepartment = "Acting",
                    name = "Tom Hanks",
                    originalName = "Tom Hanks",
                    popularity = 50.0f,
                    profilePath = "/path.jpg",
                    castId = 10,
                    character = "Forrest Gump",
                    creditId = "abc123",
                    order = 0
                )
            ),
            crew = emptyList()
        )
        coEvery { repository.getMovieCredits(movieId, language) } returns expectedCredits

        val result = useCase(movieId, language)

        assertNotNull(result)
        assertEquals(expectedCredits, result)
        assertEquals(1, result.cast.size)
        assertEquals("Tom Hanks", result.cast[0].name)
        coVerify(exactly = 1) { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke with different movie ID returns corresponding credits`() = runTest {
        val movieId = 456
        val language = "en"
        val expectedCredits = MediaCredits(
            cast = emptyList(),
            crew = listOf(
                CrewMember(
                    adult = false,
                    gender = 2,
                    id = 2,
                    knownForDepartment = "Directing",
                    name = "Christopher Nolan",
                    originalName = "Christopher Nolan",
                    popularity = 60.0f,
                    profilePath = "/nolan.jpg",
                    creditId = "xyz789",
                    department = "Directing",
                    job = "Director"
                )
            )
        )
        coEvery { repository.getMovieCredits(movieId, language) } returns expectedCredits

        val result = useCase(movieId, language)

        assertEquals(expectedCredits, result)
        assertEquals(1, result.crew.size)
        assertEquals("Christopher Nolan", result.crew[0].name)
        coVerify { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke with different language code passes it to repository`() = runTest {
        val movieId = 123
        val language = "es"
        val expectedCredits = MediaCredits.empty()
        coEvery { repository.getMovieCredits(movieId, language) } returns expectedCredits

        val result = useCase(movieId, language)

        assertEquals(expectedCredits, result)
        coVerify { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke propagates exception from repository`() = runTest {
        val movieId = 123
        val language = "en"
        val exception = RuntimeException("Network error")
        coEvery { repository.getMovieCredits(movieId, language) } throws exception

        try {
            useCase(movieId, language)
            throw AssertionError("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }

        coVerify { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke returns empty credits when repository returns empty`() = runTest {
        val movieId = 999
        val language = "en"
        val emptyCredits = MediaCredits.empty()
        coEvery { repository.getMovieCredits(movieId, language) } returns emptyCredits

        val result = useCase(movieId, language)

        assertEquals(emptyCredits, result)
        assertTrue(result.cast.isEmpty())
        assertTrue(result.crew.isEmpty())
        coVerify { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke with zero movie ID still calls repository`() = runTest {
        val movieId = 0
        val language = "en"
        val expectedCredits = MediaCredits.empty()
        coEvery { repository.getMovieCredits(movieId, language) } returns expectedCredits

        val result = useCase(movieId, language)

        assertNotNull(result)
        coVerify { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke returns credits with both cast and crew`() = runTest {
        val movieId = 550
        val language = "en"
        val creditsWithBoth = MediaCredits(
            cast = listOf(
                CastMember(
                    adult = false,
                    gender = 2,
                    id = 1,
                    knownForDepartment = "Acting",
                    name = "Brad Pitt",
                    originalName = "Brad Pitt",
                    popularity = 70.0f,
                    profilePath = "/brad.jpg",
                    castId = 1,
                    character = "Tyler Durden",
                    creditId = "cast1",
                    order = 0
                ),
                CastMember(
                    adult = false,
                    gender = 2,
                    id = 2,
                    knownForDepartment = "Acting",
                    name = "Edward Norton",
                    originalName = "Edward Norton",
                    popularity = 65.0f,
                    profilePath = "/norton.jpg",
                    castId = 2,
                    character = "The Narrator",
                    creditId = "cast2",
                    order = 1
                )
            ),
            crew = listOf(
                CrewMember(
                    adult = false,
                    gender = 2,
                    id = 3,
                    knownForDepartment = "Directing",
                    name = "David Fincher",
                    originalName = "David Fincher",
                    popularity = 80.0f,
                    profilePath = "/fincher.jpg",
                    creditId = "crew1",
                    department = "Directing",
                    job = "Director"
                )
            )
        )
        coEvery { repository.getMovieCredits(movieId, language) } returns creditsWithBoth

        val result = useCase(movieId, language)

        assertEquals(2, result.cast.size)
        assertEquals(1, result.crew.size)
        assertEquals("Brad Pitt", result.cast[0].name)
        assertEquals("Edward Norton", result.cast[1].name)
        assertEquals("David Fincher", result.crew[0].name)
        assertEquals("Director", result.crew[0].job)
        coVerify { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke can be called multiple times with same parameters`() = runTest {
        val movieId = 123
        val language = "en"
        val expectedCredits = MediaCredits.empty()
        coEvery { repository.getMovieCredits(movieId, language) } returns expectedCredits

        val result1 = useCase(movieId, language)
        val result2 = useCase(movieId, language)

        assertEquals(result1, result2)
        coVerify(exactly = 2) { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke with empty language string passes it to repository`() = runTest {
        val movieId = 123
        val language = ""
        val expectedCredits = MediaCredits.empty()
        coEvery { repository.getMovieCredits(movieId, language) } returns expectedCredits

        val result = useCase(movieId, language)

        assertNotNull(result)
        coVerify { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke returns credits with cast member details correctly`() = runTest {
        val movieId = 123
        val language = "en"
        val castMember = CastMember(
            adult = false,
            gender = 1,
            id = 100,
            knownForDepartment = "Acting",
            name = "Scarlett Johansson",
            originalName = "Scarlett Johansson",
            popularity = 85.5f,
            profilePath = "/scarlett.jpg",
            castId = 5,
            character = "Black Widow",
            creditId = "credit123",
            order = 2
        )
        val credits = MediaCredits(cast = listOf(castMember), crew = emptyList())
        coEvery { repository.getMovieCredits(movieId, language) } returns credits

        val result = useCase(movieId, language)

        val returnedCast = result.cast[0]
        assertEquals(100, returnedCast.id)
        assertEquals("Scarlett Johansson", returnedCast.name)
        assertEquals("Black Widow", returnedCast.character)
        assertEquals(85.5f, returnedCast.popularity)
        assertEquals(2, returnedCast.order)
        coVerify { repository.getMovieCredits(movieId, language) }
    }

    @Test
    fun `invoke with different locales returns localized crew jobs`() = runTest {
        val movieId = 123
        val languageEn = "en"
        val languageEs = "es"
        val creditsEn = MediaCredits(
            cast = emptyList(),
            crew = listOf(
                CrewMember(
                    adult = false, gender = 2, id = 1,
                    knownForDepartment = "Directing",
                    name = "Steven Spielberg",
                    originalName = "Steven Spielberg",
                    popularity = 90.0f,
                    profilePath = "/spielberg.jpg",
                    creditId = "crew1",
                    department = "Directing",
                    job = "Director"
                )
            )
        )
        val creditsEs = MediaCredits(
            cast = emptyList(),
            crew = listOf(
                CrewMember(
                    adult = false, gender = 2, id = 1,
                    knownForDepartment = "Dirigente",
                    name = "Steven Spielberg",
                    originalName = "Steven Spielberg",
                    popularity = 90.0f,
                    profilePath = "/spielberg.jpg",
                    creditId = "crew1",
                    department = "Dirigente",
                    job = "Director"
                )
            )
        )
        coEvery { repository.getMovieCredits(movieId, languageEn) } returns creditsEn
        coEvery { repository.getMovieCredits(movieId, languageEs) } returns creditsEs

        val resultEn = useCase(movieId, languageEn)
        val resultEs = useCase(movieId, languageEs)

        assertEquals("Director", resultEn.crew[0].job)
        assertEquals("Director", resultEs.crew[0].job)
        coVerify { repository.getMovieCredits(movieId, languageEn) }
        coVerify { repository.getMovieCredits(movieId, languageEs) }
    }
}