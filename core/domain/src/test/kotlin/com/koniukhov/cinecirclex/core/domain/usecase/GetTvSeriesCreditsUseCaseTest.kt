package com.koniukhov.cinecirclex.core.domain.usecase

import com.koniukhov.cinecirclex.core.domain.model.MediaCredits
import com.koniukhov.cinecirclex.core.domain.model.CastMember
import com.koniukhov.cinecirclex.core.domain.model.CrewMember
import com.koniukhov.cinecirclex.core.domain.repository.CreditsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTvSeriesCreditsUseCaseTest {

    private lateinit var repository: CreditsRepository
    private lateinit var useCase: GetTvSeriesCreditsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetTvSeriesCreditsUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke returns credits from repository`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val expectedCredits = MediaCredits(
            cast = listOf(
                CastMember(
                    adult = false,
                    gender = 2,
                    id = 17419,
                    knownForDepartment = "Acting",
                    name = "Bryan Cranston",
                    originalName = "Bryan Cranston",
                    popularity = 50.0f,
                    profilePath = "/7Jahy5LZX2Fo8fGJltMreAI49hC.jpg",
                    castId = 1,
                    character = "Walter White",
                    creditId = "credit1",
                    order = 0
                ),
                CastMember(
                    adult = false,
                    gender = 2,
                    id = 134531,
                    knownForDepartment = "Acting",
                    name = "Aaron Paul",
                    originalName = "Aaron Paul",
                    popularity = 40.0f,
                    profilePath = "/8Ac9iNGBg.jpg",
                    castId = 2,
                    character = "Jesse Pinkman",
                    creditId = "credit2",
                    order = 1
                )
            ),
            crew = listOf(
                CrewMember(
                    adult = false,
                    gender = 2,
                    id = 66633,
                    knownForDepartment = "Production",
                    name = "Vince Gilligan",
                    originalName = "Vince Gilligan",
                    popularity = 30.0f,
                    profilePath = "/rLSUjr725ez1cK7SKVxC9udO03Y.jpg",
                    creditId = "crew1",
                    department = "Production",
                    job = "Creator"
                )
            )
        )

        coEvery { repository.getTvSeriesCredits(tvSeriesId, languageCode) } returns expectedCredits

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedCredits, result)
        coVerify(exactly = 1) { repository.getTvSeriesCredits(tvSeriesId, languageCode) }
    }

    @Test
    fun `invoke returns credits with empty cast and crew lists`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"
        val expectedCredits = MediaCredits(
            cast = emptyList(),
            crew = emptyList()
        )

        coEvery { repository.getTvSeriesCredits(tvSeriesId, languageCode) } returns expectedCredits

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedCredits, result)
        assertEquals(0, result.cast.size)
        assertEquals(0, result.crew.size)
        coVerify(exactly = 1) { repository.getTvSeriesCredits(tvSeriesId, languageCode) }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository throws exception`() = runTest {
        val tvSeriesId = 1396
        val languageCode = "en"

        coEvery { repository.getTvSeriesCredits(tvSeriesId, languageCode) } throws Exception("Network error")

        useCase(tvSeriesId, languageCode)
    }

    @Test
    fun `invoke handles different tv series ids correctly`() = runTest {
        val tvSeriesId = 1668
        val languageCode = "en"
        val expectedCredits = MediaCredits(
            cast = listOf(
                CastMember(
                    adult = false,
                    gender = 2,
                    id = 31,
                    knownForDepartment = "Acting",
                    name = "Tom Hanks",
                    originalName = "Tom Hanks",
                    popularity = 60.0f,
                    profilePath = "/xndWFsBlClOJFRdhSt4NBwiPq2o.jpg",
                    castId = 3,
                    character = "Forrest Gump",
                    creditId = "credit3",
                    order = 0
                )
            ),
            crew = listOf(
                CrewMember(
                    adult = false,
                    gender = 2,
                    id = 7879,
                    knownForDepartment = "Directing",
                    name = "Robert Zemeckis",
                    originalName = "Robert Zemeckis",
                    popularity = 45.0f,
                    profilePath = "/lPYDQ5LYNJ12rJZENtyASmVZ1Ql.jpg",
                    creditId = "crew2",
                    department = "Directing",
                    job = "Director"
                )
            )
        )

        coEvery { repository.getTvSeriesCredits(tvSeriesId, languageCode) } returns expectedCredits

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedCredits, result)
        coVerify(exactly = 1) { repository.getTvSeriesCredits(tvSeriesId, languageCode) }
    }

    @Test
    fun `invoke handles credits with multiple cast members`() = runTest {
        val tvSeriesId = 1399
        val languageCode = "en"
        val expectedCredits = MediaCredits(
            cast = listOf(
                CastMember(
                    adult = false,
                    gender = 2,
                    id = 1,
                    knownForDepartment = "Acting",
                    name = "Actor 1",
                    originalName = "Actor 1",
                    popularity = 10.0f,
                    profilePath = "/path1.jpg",
                    castId = 10,
                    character = "Character 1",
                    creditId = "credit10",
                    order = 0
                ),
                CastMember(
                    adult = false,
                    gender = 1,
                    id = 2,
                    knownForDepartment = "Acting",
                    name = "Actor 2",
                    originalName = "Actor 2",
                    popularity = 15.0f,
                    profilePath = "/path2.jpg",
                    castId = 11,
                    character = "Character 2",
                    creditId = "credit11",
                    order = 1
                ),
                CastMember(
                    adult = false,
                    gender = 2,
                    id = 3,
                    knownForDepartment = "Acting",
                    name = "Actor 3",
                    originalName = "Actor 3",
                    popularity = 12.0f,
                    profilePath = "/path3.jpg",
                    castId = 12,
                    character = "Character 3",
                    creditId = "credit12",
                    order = 2
                )
            ),
            crew = emptyList()
        )

        coEvery { repository.getTvSeriesCredits(tvSeriesId, languageCode) } returns expectedCredits

        val result = useCase(tvSeriesId, languageCode)

        assertEquals(expectedCredits, result)
        assertEquals(3, result.cast.size)
        coVerify(exactly = 1) { repository.getTvSeriesCredits(tvSeriesId, languageCode) }
    }
}