package com.therxmv.featuremovies.domain.usecase

import com.therxmv.featuremovies.domain.repository.MoviesRepository

class RemoveFavoriteMovieUseCase(
    private val moviesRepository: MoviesRepository,
) {

    suspend operator fun invoke(id: Int) =
        moviesRepository.removeMovieFromFavorites(id)
}