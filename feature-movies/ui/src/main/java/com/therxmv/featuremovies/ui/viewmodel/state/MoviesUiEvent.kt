package com.therxmv.featuremovies.ui.viewmodel.state

sealed interface MoviesUiEvent {

    data class AddToFavorite(val movieId: Int) : MoviesUiEvent
}