package com.therxmv.featuremovies.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<MovieResponse>,
)

@Serializable
data class MovieResponse(
    @SerialName("id") val id: Int,
    @SerialName("original_title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("vote_average") val averageVote: Float,
)