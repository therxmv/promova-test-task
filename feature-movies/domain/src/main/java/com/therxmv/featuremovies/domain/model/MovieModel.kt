package com.therxmv.featuremovies.domain.model

data class MovieModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val releaseDateMillis: Long,
    val averageVote: Float,
    val isFavorite: Boolean = false,
) {
    override fun toString(): String =
        buildString {
            append("Check out ")
            if (isFavorite) {
                append("my favorite movie ")
            }
            append("\"${title}\" ")
            append("with a score of ${averageVote}/10.\n\n")

            append("It's about $overview")
        }
}