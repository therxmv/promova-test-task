package com.therxmv.featuremovies.ui

import com.therxmv.featuremovies.domain.model.MovieModel
import com.therxmv.featuremovies.ui.viewmodel.state.UiMovieItem

fun createUiModel(
    id: Int = 0,
    title: String = "title",
    description: String = "overview",
    posterUrl: String = "posterUrl",
    releaseDate: String = "Sep 2025",
    averageScore: String = "4.5",
    actions: List<UiMovieItem.Movie.Action> = emptyList(),
): UiMovieItem.Movie =
    UiMovieItem.Movie(
        id = id,
        title = title,
        description = description,
        posterUrl = posterUrl,
        releaseDate = releaseDate,
        averageScore = averageScore,
        actions = actions,
    )

fun createModel(
    id: Int = 0,
    title: String = "title",
    overview: String = "overview",
    posterUrl: String = "posterUrl",
    releaseDateMillis: Long = 0,
    averageVote: Float = 4.5f,
    isFavorite: Boolean = false,
): MovieModel =
    MovieModel(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        releaseDateMillis = releaseDateMillis,
        averageVote = averageVote,
        isFavorite = isFavorite,
    )