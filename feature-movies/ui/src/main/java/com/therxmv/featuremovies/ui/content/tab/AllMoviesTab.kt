package com.therxmv.featuremovies.ui.content.tab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.therxmv.base.ui.state.ErrorContainer
import com.therxmv.base.ui.state.LoadingContainer
import com.therxmv.base.ui.theme.PromovaTheme
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiData
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEvent
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllMoviesTab(
    pagingItems: LazyPagingItems<UiMovieItem>,
    data: MoviesUiData,
    onEvent: (MoviesUiEvent) -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = pagingItems.loadState.refresh is LoadState.Loading,
        onRefresh = pagingItems::refresh,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PromovaTheme.paddings.main),
            contentPadding = PaddingValues(PromovaTheme.paddings.main)
        ) {
            if (data.isConnected.not()) {
                item {
                    TextPlaceholder(data.noInternetText)
                }
            }

            handleRefreshPagingState(
                state = pagingItems.loadState.refresh,
                isEmpty = pagingItems.itemCount == 0,
                emptyText = data.emptyText,
                onRetry = pagingItems::retry,
            )

            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id },
                contentType = pagingItems.itemContentType(),
            ) { index ->
                val data = pagingItems[index] ?: return@items

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

            handleLoadingPagingState(pagingItems.loadState.append)
        }
    }
}

internal fun LazyListScope.handleRefreshPagingState(
    state: LoadState,
    isEmpty: Boolean,
    emptyText: String,
    onRetry: () -> Unit,
) {
    when {
        state is LoadState.NotLoading && isEmpty -> item {
            TextPlaceholder(emptyText)
        }

        state is LoadState.Error -> item {
            ErrorContainer(onRetry = onRetry)
        }
    }
}

private fun LazyListScope.handleLoadingPagingState(
    state: LoadState,
) {
    item {
        AnimatedVisibility(
            visible = state is LoadState.Loading,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            LoadingContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                size = 50.dp
            )
        }
    }
}