package com.therxmv.featuremovies.ui.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.therxmv.featuremovies.ui.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel(),
) {
    val pagingMovies = viewModel.moviesFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(
            count = pagingMovies.itemCount,
        ) { index ->
            val item = pagingMovies[index] ?: return@items

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                text = item.title,
            )
        }
    }
}