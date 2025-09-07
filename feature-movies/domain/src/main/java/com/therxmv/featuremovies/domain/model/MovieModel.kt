package com.therxmv.featuremovies.domain.model

data class MovieModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val releaseDateMillis: Long,
    val averageVote: Float,
    val isFavorite: Boolean = false, // TODO implement
)