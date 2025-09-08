package com.therxmv.featuremovies.data.repository

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.Pager
import androidx.paging.PagingData
import com.therxmv.featuremovies.data.converter.MovieConverter
import com.therxmv.featuremovies.data.createEntity
import com.therxmv.featuremovies.data.source.local.room.MoviesDatabase
import com.therxmv.featuremovies.data.source.local.room.dao.FavoriteMoviesDao
import com.therxmv.featuremovies.data.source.local.room.dao.MoviesDao
import com.therxmv.featuremovies.data.source.local.room.entity.FavoriteMovieEntity
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi
import com.therxmv.featuremovies.domain.model.MovieModel
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MoviesRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val mockMoviesNetworkApi = mockk<MoviesNetworkApi>()
    private val mockMoviesDao = mockk<MoviesDao>(relaxed = true)
    private val mockFavoriteMoviesDao = mockk<FavoriteMoviesDao>(relaxed = true)
    private val mockMoviesDatabase = mockk<MoviesDatabase>(relaxed = true) {
        every { getMoviesDao() } returns mockMoviesDao
        every { getFavoriteMoviesDao() } returns mockFavoriteMoviesDao
    }
    private val mockMovieConverter = mockk<MovieConverter>(relaxed = true)

    private val systemUnderTest = MoviesRepositoryImpl(
        moviesNetworkApi = mockMoviesNetworkApi,
        moviesDatabase = mockMoviesDatabase,
        movieConverter = mockMovieConverter,
        defaultDispatcher = testDispatcher
    )

    @Test
    fun `it creates pager and returns converted paging data`() = testScope.runTest {
        mockkConstructor(Pager::class)

        val entity = createEntity()
        every { anyConstructed<Pager<Int, MovieEntity>>().flow } returns flowOf(
            PagingData.from(
                data = listOf(entity),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.Loading,
                    prepend = LoadState.NotLoading(false),
                ),
            )
        )

        val result = systemUnderTest.getMoviesPagerFlow().firstOrNull()

        result.shouldNotBeNull()
    }

    @Test
    fun `it returns mapped favorite movies`() = testScope.runTest {
        val entity = createEntity()

        every { mockFavoriteMoviesDao.selectFavoriteMoviesEntities() } returns flowOf(listOf(entity))

        val result = systemUnderTest.getFavoriteMoviesFlow().first()

        verify(exactly = 1) { mockMovieConverter.entityToModel(entity = entity, isFavorite = true) }
        result shouldHaveSize 1
        result.first().shouldBeInstanceOf<MovieModel>()
    }

    @Test
    fun `it adds movie to favorites`() = testScope.runTest {
        systemUnderTest.addMovieToFavorites(0)

        coVerify { mockFavoriteMoviesDao.insertFavoriteMovie(FavoriteMovieEntity(0)) }
    }

    @Test
    fun `it removes movie from favorites`() = testScope.runTest {
        systemUnderTest.removeMovieFromFavorites(0)

        coVerify { mockFavoriteMoviesDao.deleteFavoriteMovie(0) }
    }
}