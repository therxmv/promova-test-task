package com.therxmv.featuremovies.data

import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.remote.dto.MovieDto

fun createEntity(
    id: Int = 0,
    title: String = "title",
    overview: String = "overview",
    posterPath: String = "posterPath",
    releaseDateMillis: Long = 0,
    averageVote: Float = 4.523f,
): MovieEntity =
    MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDateMillis = releaseDateMillis,
        averageVote = averageVote,
    )

fun createDto(
    id: Int = 0,
    title: String = "title",
    overview: String = "overview",
    posterPath: String = "posterPath",
    releaseDate: String = "2025-09-07",
    averageVote: Float = 4.5f,
): MovieDto =
    MovieDto(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        averageVote = averageVote,
    )