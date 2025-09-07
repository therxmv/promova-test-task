package com.therxmv.featuremovies.ui.viewmodel.state

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class MoviesUiData(
    val tabs: List<Tab>,
    val emptyText: String,
    val noLikesText: String,
) {
    sealed interface Tab {
        val label: String

        data class All(override val label: String) : Tab
        data class Favorite(override val label: String) : Tab
    }
}

sealed interface UiMovieItem {

    val id: Any

    @Immutable
    data class Movie(
        override val id: Int,
        val title: String,
        val description: String,
        val posterUrl: String,
        val averageScore: String,
        val releaseDate: String,
        val actions: List<Action>,
    ) : UiMovieItem {

        data class Action(
            val icon: ImageVector,
            val isEnabled: Boolean = true,
            val event: MoviesUiEvent,
        )
    }

    data class DateSeparator(
        override val id: String,
        val date: String,
    ) : UiMovieItem
}