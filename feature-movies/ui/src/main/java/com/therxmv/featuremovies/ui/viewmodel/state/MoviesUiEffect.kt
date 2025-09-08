package com.therxmv.featuremovies.ui.viewmodel.state

sealed interface MoviesUiEffect {
    data class ShareMovieDetails(val details: String) : MoviesUiEffect
}