package com.therxmv.featuremovies.data.converter

import com.therxmv.base.date.toLocalDate
import com.therxmv.base.date.toMilliseconds
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.remote.dto.MovieDto
import com.therxmv.featuremovies.data.source.remote.dto.MoviesResponse
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class MovieConverterTest {

    private val mockPosterUrlFactory = mockk<PosterUrlFactory> {
        every { create(any()) } returns "url"
    }

    private val systemUnderTest = MovieConverter(
        posterUrlFactory = mockPosterUrlFactory,
    )

    @Test
    fun `it converts MoviesResponse with page 1 to MoviePageEntity`() {
        val dto = createDto()
        val response = MoviesResponse(
            page = 1,
            results = listOf(dto),
            totalPages = 10,
        )

        val result = systemUnderTest.responseToPageEntity(response)

        result shouldHaveSize 1

        with(result.first()) {
            this.movieId shouldBe dto.id
            prevPage shouldBe null
            nextPage shouldBe 2
        }
    }

    @Test
    fun `it converts MoviesResponse with page 3 to MoviePageEntity`() {
        val dto = createDto()
        val response = MoviesResponse(
            page = 3,
            results = listOf(dto),
            totalPages = 10,
        )

        val result = systemUnderTest.responseToPageEntity(response)

        result shouldHaveSize 1

        with(result.first()) {
            this.movieId shouldBe dto.id
            prevPage shouldBe 2
            nextPage shouldBe 4
        }
    }

    @Test
    fun `it converts MoviesResponse with page 10 to MoviePageEntity`() {
        val dto = createDto()
        val response = MoviesResponse(
            page = 10,
            results = listOf(dto),
            totalPages = 10,
        )

        val result = systemUnderTest.responseToPageEntity(response)

        result shouldHaveSize 1

        with(result.first()) {
            this.movieId shouldBe dto.id
            prevPage shouldBe 9
            nextPage shouldBe null
        }
    }

    @Test
    fun `it converts list of MovieDto to list of MovieEntity`() {
        val dto = createDto()
        val dtos = listOf(dto)

        val result = systemUnderTest.dtoToEntity(dtos)

        result shouldHaveSize 1

        with(result.first()) {
            this.id shouldBe dto.id
            releaseDateMillis shouldBe dto.releaseDate.toLocalDate().toMilliseconds()
        }
    }

    @Test
    fun `it converts MovieEntity to MovieModel and sets isFavorite`() {
        val entity = createEntity()

        val result = systemUnderTest.entityToModel(entity = entity, isFavorite = true)

        result.id shouldBe entity.id
        result.isFavorite shouldBe true
    }

    @Test
    fun `it converts MovieEntity to MovieModel and rounds average vote`() {
        val votes = listOf(4.5123f, 4.012f, 5.056f)
        val entity = votes.map { createEntity(averageVote = it) }

        val result = entity.map {
            systemUnderTest.entityToModel(it)
        }

        val expectedVotes = listOf(4.5f, 4.0f, 5.1f)
        result.map { it.averageVote } shouldBe expectedVotes
    }

    private fun createEntity(
        id: Int = 0,
        title: String = "title",
        overview: String = "overview",
        posterPath: String = "posterPath",
        releaseDateMillis: Long = 0,
        averageVote: Float = 4.523f,
    ): MovieEntity =
        MovieEntity(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            releaseDateMillis = releaseDateMillis,
            averageVote = averageVote,
        )

    private fun createDto(
        id: Int = 0,
        title: String = "title",
        overview: String = "overview",
        posterPath: String = "posterPath",
        releaseDate: String = "2025-09-07",
        averageVote: Float = 4.5f,
    ): MovieDto =
        MovieDto(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate,
            averageVote = averageVote,
        )
}