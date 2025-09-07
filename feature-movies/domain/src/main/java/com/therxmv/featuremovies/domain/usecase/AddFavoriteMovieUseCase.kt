package com.therxmv.featuremovies.domain.usecase

import com.therxmv.featuremovies.domain.repository.MoviesRepository

class AddFavoriteMovieUseCase(
    private val moviesRepository: MoviesRepository,
) {

    suspend operator fun invoke(id: Int) =
        moviesRepository.addMovieToFavorites(id)
}