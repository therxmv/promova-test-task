package com.therxmv.featuremovies.data.converter

import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.remote.dto.MovieDto
import com.therxmv.featuremovies.domain.model.MovieModel
import kotlin.math.round

class MovieConverter(
    private val posterUrlFactory: PosterUrlFactory,
) {

    fun entityToModel(entity: MovieEntity): MovieModel =
        with(entity) {
            MovieModel(
                id = id,
                title = title,
                overview = overview,
                posterUrl = posterUrlFactory.create(posterPath),
                releaseDate = releaseDate,
                averageVote = round(averageVote * 10) / 10,
            )
        }

    fun dtoToEntity(list: List<MovieDto>): List<MovieEntity> =
        list.map {
            with(it) {
                MovieEntity(
                    id = id,
                    title = title,
                    overview = overview,
                    posterPath = posterPath,
                    releaseDate = releaseDate,
                    averageVote = averageVote,
                )
            }
        }
}