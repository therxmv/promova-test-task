package com.therxmv.featuremovies.domain.usecase

import com.therxmv.featuremovies.domain.model.MovieModel
import com.therxmv.featuremovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMoviesFlowUseCase(
    private val moviesRepository: MoviesRepository,
) {

    operator fun invoke(): Flow<List<MovieModel>> =
        moviesRepository.getFavoriteMoviesFlow()
}