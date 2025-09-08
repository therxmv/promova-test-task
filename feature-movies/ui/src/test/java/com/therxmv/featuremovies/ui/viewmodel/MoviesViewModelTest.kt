package com.therxmv.featuremovies.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.therxmv.base.network.ConnectivityObserver
import com.therxmv.featuremovies.domain.usecase.AddFavoriteMovieUseCase
import com.therxmv.featuremovies.domain.usecase.GetFavoriteMoviesFlowUseCase
import com.therxmv.featuremovies.domain.usecase.GetMoviesPagerFlowUseCase
import com.therxmv.featuremovies.domain.usecase.RemoveFavoriteMovieUseCase
import com.therxmv.featuremovies.ui.createModel
import com.therxmv.featuremovies.ui.createUiModel
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEvent
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiState
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    val testDispatcher = StandardTestDispatcher()
    val testScope = TestScope(testDispatcher)

    private val mockGetMoviesPagerFlow = mockk<GetMoviesPagerFlowUseCase> {
        every { this@mockk.invoke() } returns flowOf(
            PagingData.from(
                data = listOf(createModel()),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.Loading,
                    prepend = LoadState.NotLoading(false),
                ),
            )
        )
    }
    private val mockGetFavoriteMoviesFlow = mockk<GetFavoriteMoviesFlowUseCase> {
        every { this@mockk.invoke() } returns flowOf(listOf(createModel(isFavorite = true)))
    }
    private val mockAddFavoriteMovie = mockk<AddFavoriteMovieUseCase>(relaxed = true)
    private val mockRemoveFavoriteMovie = mockk<RemoveFavoriteMovieUseCase>(relaxed = true)
    private val mockConnectivityObserver = mockk<ConnectivityObserver> {
        every { isConnectedFlow } returns MutableStateFlow(true)
    }

    private lateinit var systemUnderTest: MoviesViewModel

    @Test
    fun `initial state is Idle and connected status is not updated`() = testScope.runTest {
        assumeViewModelCreated()

        val result = systemUnderTest.uiState.firstOrNull()

        result.shouldBeInstanceOf<MoviesUiState.Idle>()
    }

    @Test
    fun `it creates Ready state with isConnected=true`() = testScope.runTest {
        assumeViewModelCreated()
        advanceUntilIdle()

        val result = systemUnderTest.uiState.firstOrNull()

        result.shouldBeInstanceOf<MoviesUiState.Ready>()
        result.data.isConnected.shouldBeTrue()
    }

    @Test
    fun `it creates Ready state with two tabs`() = testScope.runTest {
        assumeViewModelCreated()
        advanceUntilIdle()

        val result = systemUnderTest.uiState.firstOrNull()

        result.shouldBeInstanceOf<MoviesUiState.Ready>()
        result.data.tabs shouldHaveSize 2
    }

    @Test
    fun `it adds movie to favorite on ToggleFavoriteMovie event`() = testScope.runTest {
        assumeViewModelCreated()

        systemUnderTest.onEvent(MoviesUiEvent.ToggleFavoriteMovie(movieId = 0, isFavorite = false))

        coVerify { mockAddFavoriteMovie(0) }
    }

    @Test
    fun `it removes movie from favorite on ToggleFavoriteMovie event`() = testScope.runTest {
        assumeViewModelCreated()

        systemUnderTest.onEvent(MoviesUiEvent.ToggleFavoriteMovie(movieId = 0, isFavorite = true))

        coVerify { mockRemoveFavoriteMovie(0) }
    }

    @Test
    fun `it converts favorite movies flow to ui model with date separators`() = testScope.runTest {
        every { mockGetFavoriteMoviesFlow() } returns flowOf(
            listOf(1757278800000, 1757192400000, 1754946000000).map {
                createModel(releaseDateMillis = it, isFavorite = true)
            }
        )

        assumeViewModelCreated()
        advanceUntilIdle()

        val result = systemUnderTest.favoriteMoviesState.firstOrNull()

        val actions = listOf(
            UiMovieItem.Movie.Action(
                icon = Icons.Default.Star,
                isEnabled = true,
                event = MoviesUiEvent.ToggleFavoriteMovie(movieId = 0, isFavorite = true),
            ),
        )
        val expectedList = listOf(
            UiMovieItem.DateSeparator("Sep 2025", "Sep 2025"),
            createUiModel(releaseDate = "Sep 2025", actions = actions),
            createUiModel(releaseDate = "Sep 2025", actions = actions),
            UiMovieItem.DateSeparator("Aug 2025", "Aug 2025"),
            createUiModel(releaseDate = "Aug 2025", actions = actions),
        )

        result.shouldNotBeNull()
        result shouldBe expectedList
    }

    // TODO test paging

    private fun assumeViewModelCreated() {
        systemUnderTest = MoviesViewModel(
            getMoviesPagerFlow = mockGetMoviesPagerFlow,
            getFavoriteMoviesFlow = mockGetFavoriteMoviesFlow,
            addFavoriteMovie = mockAddFavoriteMovie,
            removeFavoriteMovie = mockRemoveFavoriteMovie,
            connectivityObserver = mockConnectivityObserver,
            defaultDispatcher = testDispatcher,
            ioDispatcher = testDispatcher,
        )
    }
}