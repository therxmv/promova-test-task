package com.therxmv.featuremovies.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.therxmv.base.ui.state.ErrorContainer
import com.therxmv.base.ui.state.LoadingContainer
import com.therxmv.featuremovies.ui.viewmodel.MoviesViewModel
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    val pagingMovies = viewModel.moviesFlow.collectAsLazyPagingItems()

    when (uiState) {
        is MoviesUiState.Ready -> MoviesContent(
            data = uiState.data,
            pagingMovies = pagingMovies,
            favoriteMovies = viewModel.favoriteMoviesState,
            onEvent = viewModel::onEvent,
        )

        MoviesUiState.Loading -> LoadingContainer()

        MoviesUiState.Error -> ErrorContainer()

        MoviesUiState.Idle -> Unit
    }
}