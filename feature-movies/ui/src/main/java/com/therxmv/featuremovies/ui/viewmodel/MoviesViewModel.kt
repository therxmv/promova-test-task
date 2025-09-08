package com.therxmv.featuremovies.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.therxmv.base.date.formatToMonthAndYear
import com.therxmv.base.network.ConnectivityObserver
import com.therxmv.featuremovies.domain.model.MovieModel
import com.therxmv.featuremovies.domain.usecase.AddFavoriteMovieUseCase
import com.therxmv.featuremovies.domain.usecase.GetFavoriteMoviesFlowUseCase
import com.therxmv.featuremovies.domain.usecase.GetMoviesPagerFlowUseCase
import com.therxmv.featuremovies.domain.usecase.RemoveFavoriteMovieUseCase
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiData
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEffect
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEvent
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiState
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(
    getMoviesPagerFlow: GetMoviesPagerFlowUseCase,
    getFavoriteMoviesFlow: GetFavoriteMoviesFlowUseCase,
    private val addFavoriteMovie: AddFavoriteMovieUseCase,
    private val removeFavoriteMovie: RemoveFavoriteMovieUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<MoviesUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    val moviesFlow: Flow<PagingData<UiMovieItem>> = getMoviesPagerFlow()
        .cachedIn(viewModelScope)
        .combine(getFavoriteMoviesFlow()) { data, favorites ->
            data
                .map { movie ->
                    val isFavorite = favorites.find { it.id == movie.id } != null
                    movie.copy(isFavorite = isFavorite).mapToUiItem()
                }
                .insertDateSeparators()
        }

    val favoriteMoviesState = getFavoriteMoviesFlow()
        .map { list ->
            list
                .map { it.mapToUiItem() }
                .insertDateSeparators()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
        )

    init {
        observeConnection()
        loadData()
    }

    fun onEvent(event: MoviesUiEvent) {
        when (event) {
            is MoviesUiEvent.ToggleFavoriteMovie -> toggleFavoriteMovie(movieId = event.movieId, isFavorite = event.isFavorite)

            is MoviesUiEvent.ShareMovie -> shareMovie(event.movie)
        }
    }

    private fun observeConnection() {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler()) {
            connectivityObserver.isConnectedFlow.collectLatest { isConnected ->
                _uiState.update { state ->
                    if (state is MoviesUiState.Ready) {
                        state.copy(
                            data = state.data.copy(isConnected = isConnected)
                        )
                    } else {
                        state
                    }
                }
            }
        }
    }

    fun loadData() {
        viewModelScope.launch(
            ioDispatcher + coroutineExceptionHandler { _uiState.update { MoviesUiState.Error } }
        ) {
            _uiState.update { MoviesUiState.Loading }
            delay(1000) // Intended delay to simulate data loading (e.g. translated strings)

            _uiState.update {
                MoviesUiState.Ready(
                    data = MoviesUiData(
                        tabs = createTabs(),
                        emptyText = "There are no items to display!",
                        noLikesText = "You haven't liked any movie yet",
                        noInternetText = "You don't have internet connection but we saved some movies for you ;)",
                        isConnected = connectivityObserver.isConnectedFlow.firstOrNull() == true,
                    ),
                )
            }
        }
    }

    private fun toggleFavoriteMovie(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler()) {
            if (isFavorite) {
                removeFavoriteMovie(movieId)
            } else {
                addFavoriteMovie(movieId)
            }
        }
    }

    private fun shareMovie(movie: MovieModel) {
        viewModelScope.launch {
            _uiEffect.send(
                MoviesUiEffect.ShareMovieDetails(details = movie.toString())
            )
        }
    }

    private fun coroutineExceptionHandler(
        onError: (Throwable) -> Unit = { it.printStackTrace() },
    ): CoroutineExceptionHandler = CoroutineExceptionHandler { context, throwable -> onError(throwable) }

    private fun createTabs(): List<MoviesUiData.Tab> =
        listOf(
            MoviesUiData.Tab.All("Movies"),
            MoviesUiData.Tab.Favorite("Favorites"),
        )

    private fun MovieModel.getMovieActions(): List<UiMovieItem.Movie.Action> =
        listOf(
            UiMovieItem.Movie.Action(
                icon = Icons.Default.Share,
                isEnabled = true,
                event = MoviesUiEvent.ShareMovie(movie = this),
            ),
            UiMovieItem.Movie.Action(
                icon = Icons.Default.Star,
                isEnabled = isFavorite,
                event = MoviesUiEvent.ToggleFavoriteMovie(movieId = id, isFavorite = isFavorite),
            ),
        )

    private suspend fun MovieModel.mapToUiItem(): UiMovieItem =
        withContext(defaultDispatcher) {
            UiMovieItem.Movie(
                id = id,
                title = title,
                description = overview,
                posterUrl = posterUrl,
                averageScore = averageVote.toString(),
                releaseDate = releaseDateMillis.formatToMonthAndYear(),
                actions = getMovieActions(),
            )
        }

    private fun PagingData<UiMovieItem>.insertDateSeparators(): PagingData<UiMovieItem> =
        insertSeparators(TerminalSeparatorType.SOURCE_COMPLETE) { prev, next ->
            val prevDate = (prev as? UiMovieItem.Movie)?.releaseDate
            val nextDate = (next as? UiMovieItem.Movie)?.releaseDate

            shouldInsertDate(prevDate, nextDate)
        }

    private fun List<UiMovieItem>.insertDateSeparators(): List<UiMovieItem> {
        if (isEmpty()) return this

        val result = mutableListOf<UiMovieItem>()
        var prevDate: String? = null

        forEach { item ->
            val nextDate = (item as? UiMovieItem.Movie)?.releaseDate

            shouldInsertDate(prevDate, nextDate)?.let { result += it }
            result += item

            prevDate = nextDate
        }

        return result
    }

    private fun shouldInsertDate(prevDate: String?, nextDate: String?): UiMovieItem.DateSeparator? =
        when {
            nextDate == null -> null

            prevDate == null || prevDate != nextDate -> UiMovieItem.DateSeparator(
                id = nextDate,
                date = nextDate,
            )

            else -> null
        }
}