package com.therxmv.featuremovies.ui.viewmodel.state

import com.therxmv.featuremovies.domain.model.MovieModel

sealed interface MoviesUiEvent {

    data class ToggleFavoriteMovie(val movieId: Int, val isFavorite: Boolean) : MoviesUiEvent

    data class ShareMovie(val movie: MovieModel) : MoviesUiEvent
}