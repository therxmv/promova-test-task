package com.therxmv.featuremovies.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.therxmv.base.date.formatToMonthAndYear
import com.therxmv.featuremovies.domain.model.MovieModel
import com.therxmv.featuremovies.domain.usecase.AddFavoriteMovieUseCase
import com.therxmv.featuremovies.domain.usecase.GetFavoriteMoviesFlowUseCase
import com.therxmv.featuremovies.domain.usecase.GetMoviesPagerFlowUseCase
import com.therxmv.featuremovies.domain.usecase.RemoveFavoriteMovieUseCase
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiData
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEvent
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiState
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    getMoviesPagerFlow: GetMoviesPagerFlowUseCase,
    getFavoriteMoviesFlow: GetFavoriteMoviesFlowUseCase,
    private val addFavoriteMovie: AddFavoriteMovieUseCase,
    private val removeFavoriteMovie: RemoveFavoriteMovieUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Idle)
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
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
        loadData()
    }

    fun onEvent(event: MoviesUiEvent) {
        when (event) {
            is MoviesUiEvent.ToggleFavoriteMovie -> toggleFavoriteMovie(movieId = event.movieId, isFavorite = event.isFavorite)
        }
    }

    private fun loadData() {
        _uiState.update {
            MoviesUiState.Ready(
                data = MoviesUiData(
                    tabs = createTabs(),
                ),
            )
        }
    }

    private fun toggleFavoriteMovie(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                removeFavoriteMovie(movieId)
            } else {
                addFavoriteMovie(movieId)
            }
        }
    }

    private fun createTabs(): List<MoviesUiData.Tab> =
        listOf(
            MoviesUiData.Tab.All("Movies"),
            MoviesUiData.Tab.Favorite("Favorites"),
        )

    private fun MovieModel.getMovieActions(): List<UiMovieItem.Movie.Action> =
        listOf(
//            UiMovieItem.Movie.Action( // TODO
//                icon = Icons.Default.Share,
//                event = MoviesUiEvent,
//            ),
            UiMovieItem.Movie.Action(
                icon = Icons.Default.Star,
                isEnabled = isFavorite,
                event = MoviesUiEvent.ToggleFavoriteMovie(movieId = id, isFavorite = isFavorite),
            ),
        )

    private fun MovieModel.mapToUiItem(): UiMovieItem =
        UiMovieItem.Movie(
            id = id,
            title = title,
            description = overview,
            posterUrl = posterUrl,
            averageScore = averageVote.toString(),
            releaseDate = releaseDateMillis.formatToMonthAndYear(),
            actions = getMovieActions(),
        )

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