package com.therxmv.featuremovies.ui.viewmodel.state

sealed interface MoviesUiState {

    data object Idle : MoviesUiState

    data object Loading : MoviesUiState

    data class Ready(val data: MoviesUiData) : MoviesUiState

    data object Error : MoviesUiState
}