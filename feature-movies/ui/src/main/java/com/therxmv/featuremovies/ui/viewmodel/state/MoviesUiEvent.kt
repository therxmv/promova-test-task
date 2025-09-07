package com.therxmv.featuremovies.ui.viewmodel.state

sealed interface MoviesUiEvent {

    data class ToggleFavoriteMovie(val movieId: Int, val isFavorite: Boolean) : MoviesUiEvent
}