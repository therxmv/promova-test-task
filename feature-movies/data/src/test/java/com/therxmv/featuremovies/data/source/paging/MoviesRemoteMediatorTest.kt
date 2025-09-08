package com.therxmv.featuremovies.data.source.paging

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.therxmv.featuremovies.data.converter.MovieConverter
import com.therxmv.featuremovies.data.createDto
import com.therxmv.featuremovies.data.createEntity
import com.therxmv.featuremovies.data.source.local.room.MoviesDatabase
import com.therxmv.featuremovies.data.source.local.room.dao.MoviePageDao
import com.therxmv.featuremovies.data.source.local.room.dao.MoviesDao
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.local.room.entity.MoviePageEntity
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi
import com.therxmv.featuremovies.data.source.remote.dto.MoviesResponse
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediatorTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val totalPages = 10
    private val mockMoviesNetworkApi = mockk<MoviesNetworkApi> {
        coEvery { getMoviesByPage(any()) } answers {
            MoviesResponse(
                page = arg<Int>(0),
                results = listOf(createDto()),
                totalPages = totalPages,
            )
        }
    }
    private val mockMoviesDao = mockk<MoviesDao>(relaxed = true)
    private val mockMoviesPageDao = mockk<MoviePageDao>(relaxed = true)
    private val mockMoviesDatabase = mockk<MoviesDatabase> {
        every { getMoviesDao() } returns mockMoviesDao
        every { getMoviePageDao() } returns mockMoviesPageDao
    }
    private val mockMovieConverter = mockk<MovieConverter>(relaxed = true)

    private val systemUnderTest = MoviesRemoteMediator(
        moviesNetworkApi = mockMoviesNetworkApi,
        moviesDatabase = mockMoviesDatabase,
        movieConverter = mockMovieConverter,
        defaultDispatcher = testDispatcher,
    )

    @Before
    fun setUp() {
        mockkStatic("androidx.room.RoomDatabaseKt")

        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { mockMoviesDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `it loads page 1 on Refresh and end is not reached`() = testScope.runTest {
        val result = systemUnderTest.load(LoadType.REFRESH, createPagingState())

        coVerify { mockMoviesNetworkApi.getMoviesByPage(1) }

        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached.shouldBeFalse()
    }

    @Test
    fun `it does not load data on Prepend`() = testScope.runTest {
        val result = systemUnderTest.load(LoadType.PREPEND, createPagingState())

        coVerify(exactly = 0) { mockMoviesNetworkApi.getMoviesByPage(1) }

        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached.shouldBeTrue()
    }

    @Test
    fun `it loads next page on Append and end is not reached`() = testScope.runTest {
        val result = systemUnderTest.load(LoadType.APPEND, createPagingState())

        coVerify { mockMoviesNetworkApi.getMoviesByPage(2) }

        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached.shouldBeFalse()
    }

    @Test
    fun `it loads next page on Append and end is reached`() = testScope.runTest {
        coEvery { mockMoviesPageDao.selectPageById(any()) } answers {
            MoviePageEntity(movieId = arg<Int>(0), prevPage = 0, nextPage = arg<Int>(0))
        }

        val results = (1..10).map { id ->
            val state = createPagingState(
                pages = listOf(
                    PagingSource.LoadResult.Page(
                        data = listOf(createEntity(id = id)),
                        prevKey = 1,
                        nextKey = 2,
                    )
                ),
            )

            systemUnderTest.load(LoadType.APPEND, state)
        }

        coVerify(exactly = 10) { mockMoviesNetworkApi.getMoviesByPage(any()) }

        results shouldHaveSize 10

        val lastResult = results.last()
        lastResult.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        lastResult.endOfPaginationReached.shouldBeTrue()
    }

    @Test
    fun `it clears pages and movies on Refresh`() = testScope.runTest {
        val result = systemUnderTest.load(LoadType.REFRESH, createPagingState())

        coVerify(exactly = 1) {
            mockMoviesPageDao.deletePages()
            mockMoviesDao.deleteMovies()
        }

        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached.shouldBeFalse()
    }

    @Test
    fun `it saves pages and movies and does not clear old on Append`() = testScope.runTest {
        val result = systemUnderTest.load(LoadType.APPEND, createPagingState())

        coVerify(exactly = 0) {
            mockMoviesPageDao.deletePages()
            mockMoviesDao.deleteMovies()
        }

        coVerify {
            mockMoviesPageDao.insertPages(any())
            mockMoviesDao.insertMovies(any())
        }

        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached.shouldBeFalse()
    }

    @Test
    fun `it returns Error when exception was thrown and database is empty`() = testScope.runTest {
        coEvery { mockMoviesDao.getCount() } returns 0
        coEvery { mockMoviesNetworkApi.getMoviesByPage(any()) } throws Exception()

        val result = systemUnderTest.load(LoadType.REFRESH, createPagingState())

        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Error>()
    }

    @Test
    fun `it returns Success despite exception was thrown when database is not empty`() = testScope.runTest {
        coEvery { mockMoviesDao.getCount() } returns 1
        coEvery { mockMoviesNetworkApi.getMoviesByPage(any()) } throws Exception()

        val result = systemUnderTest.load(LoadType.REFRESH, createPagingState())

        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        result.endOfPaginationReached.shouldBeTrue()
    }

    private fun createPagingState(
        pages: List<PagingSource.LoadResult.Page<Int, MovieEntity>> = emptyList(),
    ): PagingState<Int, MovieEntity> =
        PagingState(
            pages = pages,
            anchorPosition = null,
            config = PagingConfig(PAGE_SIZE),
            leadingPlaceholderCount = 10
        )
}