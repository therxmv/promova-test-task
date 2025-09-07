package com.therxmv.featuremovies.data.repository

import com.therxmv.featuremovies.data.converter.MovieConverter
import com.therxmv.featuremovies.data.createEntity
import com.therxmv.featuremovies.data.source.local.room.MoviesDatabase
import com.therxmv.featuremovies.data.source.local.room.dao.FavoriteMoviesDao
import com.therxmv.featuremovies.data.source.local.room.dao.MoviesDao
import com.therxmv.featuremovies.data.source.local.room.entity.FavoriteMovieEntity
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi
import com.therxmv.featuremovies.domain.model.MovieModel
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MoviesRepositoryImplTest {
    val testDispatcher = StandardTestDispatcher()
    val testScope = TestScope(testDispatcher)

    val mockNetworkApi = mockk<MoviesNetworkApi>()
    val mockMoviesDao = mockk<MoviesDao>(relaxed = true)
    val mockFavoriteDao = mockk<FavoriteMoviesDao>(relaxed = true)
    val mockDatabase = mockk<MoviesDatabase> {
        every { getMoviesDao() } returns mockMoviesDao
        every { getFavoriteMoviesDao() } returns mockFavoriteDao
    }
    val mockConverter = mockk<MovieConverter>(relaxed = true)

    val systemUnderTest = MoviesRepositoryImpl(
        moviesNetworkApi = mockNetworkApi,
        moviesDatabase = mockDatabase,
        movieConverter = mockConverter,
        defaultDispatcher = testDispatcher
    )

//    @Test // TODO
//    fun `it creates pager and returns converted paging data`() {
//
//    }

    @Test
    fun `it returns mapped favorite movies`() = testScope.runTest {
        val entity = createEntity()

        every { mockFavoriteDao.selectFavoriteMoviesEntities() } returns flowOf(listOf(entity))

        val result = systemUnderTest.getFavoriteMoviesFlow().first()

        verify(exactly = 1) { mockConverter.entityToModel(entity = entity, isFavorite = true) }
        result shouldHaveSize 1
        result.first().shouldBeInstanceOf<MovieModel>()
    }

    @Test
    fun `it adds movie to favorites`() = testScope.runTest {
        systemUnderTest.addMovieToFavorites(0)

        coVerify { mockFavoriteDao.insertFavoriteMovie(FavoriteMovieEntity(0)) }
    }

    @Test
    fun `it removes movie from favorites`() = testScope.runTest {
        systemUnderTest.removeMovieFromFavorites(0)

        coVerify { mockFavoriteDao.deleteFavoriteMovie(0) }
    }
}