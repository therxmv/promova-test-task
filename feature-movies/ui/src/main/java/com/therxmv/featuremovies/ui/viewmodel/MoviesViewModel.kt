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
import com.therxmv.featuremovies.domain.usecase.GetMoviesPagerFlowUseCase
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiData
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEvent
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiState
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MoviesViewModel(
    getMoviesPagerFlowUseCase: GetMoviesPagerFlowUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Idle)
    val uiState = _uiState.asStateFlow()

    val moviesFlow: Flow<PagingData<UiMovieItem>> = getMoviesPagerFlowUseCase()
        .map { data ->
            data
                .mapToUiItems()
                .insertDateSeparators()
        }
        .cachedIn(viewModelScope)

    init {
        loadData()
    }

    fun onEvent(event: MoviesUiEvent) {
        when (event) {
            is MoviesUiEvent.AddToFavorite -> {

            }
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

    private fun createTabs(): List<String> =
        listOf(
            "Movies",
            "Favorites",
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
                event = MoviesUiEvent.AddToFavorite(movieId = id),
            ),
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun PagingData<MovieModel>.mapToUiItems(): PagingData<UiMovieItem> =
        map { movieModel ->
            with(movieModel) {
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
        }

    private fun PagingData<UiMovieItem>.insertDateSeparators(): PagingData<UiMovieItem> =
        insertSeparators(TerminalSeparatorType.SOURCE_COMPLETE) { prev, next ->
            val prevDate = (prev as? UiMovieItem.Movie)?.releaseDate
            val nextDate = (next as? UiMovieItem.Movie)?.releaseDate

            when {
                nextDate == null -> null

                prevDate == null || prevDate != nextDate -> UiMovieItem.DateSeparator(
                    id = nextDate,
                    date = nextDate,
                )

                else -> null
            }
        }
}