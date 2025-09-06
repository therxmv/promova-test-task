package com.therxmv.featuremovies.domain.usecase

import androidx.paging.PagingData
import com.therxmv.featuremovies.domain.model.MovieModel
import com.therxmv.featuremovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesPagerFlowUseCase(
    private val moviesRepository: MoviesRepository,
) {

    operator fun invoke(): Flow<PagingData<MovieModel>> =
        moviesRepository.getMoviesPagerFlow()
}