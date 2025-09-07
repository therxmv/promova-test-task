package com.therxmv.featuremovies.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import com.therxmv.base.ui.theme.PromovaTheme
import com.therxmv.featuremovies.ui.content.tab.AllMoviesTab
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiData
import com.therxmv.featuremovies.ui.viewmodel.state.MoviesUiEvent
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem
import kotlinx.coroutines.launch

@Composable
internal fun MoviesContent(
    data: MoviesUiData,
    pagingMovies: LazyPagingItems<UiMovieItem>,
    onEvent: (MoviesUiEvent) -> Unit,
) {
    val pagerState = rememberPagerState { data.tabs.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize(),
    ) {
        HorizontalTabs(
            tabs = data.tabs,
            currentPage = pagerState.currentPage,
            onClick = { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            },
        )

        PagerTabContent(
            pagerState = pagerState,
            pagingMovies = pagingMovies,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun PagerTabContent(
    pagerState: PagerState,
    pagingMovies: LazyPagingItems<UiMovieItem>,
    onEvent: (MoviesUiEvent) -> Unit,
) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) { page ->
        when (page) {
            0 -> AllMoviesTab(
                pagingItems = pagingMovies,
                onEvent = onEvent,
            )

            1 -> Text("second")
        }
    }
}

@Composable
private fun HorizontalTabs(
    tabs: List<String>,
    currentPage: Int,
    onClick: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = currentPage,
    ) {
        tabs.forEachIndexed { index, label ->
            Tab(
                selected = currentPage == index,
                onClick = { onClick(index) },
                text = {
                    Text(
                        text = label,
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalTabsPreview() {
    PromovaTheme {
        HorizontalTabs(
            tabs = listOf("Tab1", "Tab2"),
            currentPage = 0,
            onClick = {},
        )
    }
}