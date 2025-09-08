package com.therxmv.featuremovies.ui.content

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.therxmv.base.ui.context.startIntentChooser
import com.therxmv.base.ui.state.ErrorContainer
import com.therxmv.base.ui.state.LoadingContainer
import com.therxmv.featuremovies.ui.viewmodel.MoviesViewModel
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEffect
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val uiEffect = viewModel.uiEffect.collectAsStateWithLifecycle(null).value
    val pagingMovies = viewModel.moviesFlow.collectAsLazyPagingItems()

    when (uiState) {
        is MoviesUiState.Ready -> MoviesContent(
            data = uiState.data,
            pagingMovies = pagingMovies,
            favoriteMovies = viewModel.favoriteMoviesState,
            onEvent = viewModel::onEvent,
        )

        MoviesUiState.Loading -> LoadingContainer()

        MoviesUiState.Error -> ErrorContainer(onRetry = viewModel::loadData)

        MoviesUiState.Idle -> Unit
    }

    LaunchedEffect(uiEffect) {
        when (uiEffect) {
            is MoviesUiEffect.ShareMovieDetails -> {
                context.startIntentChooser(
                    action = Intent.ACTION_SEND,
                    intentBuilder = {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, uiEffect.details)
                    },
                )
            }

            null -> Unit
        }
    }
}