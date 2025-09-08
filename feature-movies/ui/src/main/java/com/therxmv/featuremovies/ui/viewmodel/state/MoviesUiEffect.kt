package com.therxmv.featuremovies.ui.viewmodel.state

sealed interface MoviesUiEffect {
    class ShareMovieDetails(val details: String) : MoviesUiEffect
}