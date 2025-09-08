package com.therxmv.featuremovies.ui.content.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.therxmv.base.ui.theme.PromovaTheme
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEvent
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun FavoriteMoviesTab(
    favoriteMovies: StateFlow<List<UiMovieItem>>,
    emptyText: String,
    onEvent: (MoviesUiEvent) -> Unit,
) {
    val movies by favoriteMovies.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(PromovaTheme.paddings.main),
        contentPadding = PaddingValues(PromovaTheme.paddings.main)
    ) {
        if (movies.isEmpty()) {
            item {
                TextPlaceholder(emptyText)
            }
        }

        items(
            items = movies,
            key = { it.id },
        ) { data ->
            when (data) {
                is UiMovieItem.DateSeparator -> DateSeparator(
                    modifier = Modifier.animateItem(),
                    date = data.date,
                )

                is UiMovieItem.Movie -> MovieItem(
                    modifier = Modifier.animateItem(),
                    data = data,
                    onEvent = onEvent,
                )
            }
        }
    }
}