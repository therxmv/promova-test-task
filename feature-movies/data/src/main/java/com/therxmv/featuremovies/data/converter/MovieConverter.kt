package com.therxmv.featuremovies.data.converter

import com.therxmv.base.date.toLocalDate
import com.therxmv.base.date.toMilliseconds
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.local.room.entity.MoviePageEntity
import com.therxmv.featuremovies.data.source.remote.dto.MovieDto
import com.therxmv.featuremovies.data.source.remote.dto.MoviesResponse
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
                releaseDateMillis = releaseDateMillis,
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
                    releaseDateMillis = releaseDate.toLocalDate().toMilliseconds(),
                    averageVote = averageVote,
                )
            }
        }

    fun responseToKeyEntity(response: MoviesResponse): List<MoviePageEntity> {
        val nextPage = response.page.plus(1).takeIf { it <= response.totalPages }
        val prevPage = response.page.minus(1).takeIf { it >= 0 }

        return response.results.map { movie ->
            MoviePageEntity(
                movieId = movie.id,
                nextPage = nextPage,
                prevPage = prevPage,
            )
        }
    }
}