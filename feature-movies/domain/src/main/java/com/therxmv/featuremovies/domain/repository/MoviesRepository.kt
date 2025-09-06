package com.therxmv.featuremovies.domain.repository

import androidx.paging.PagingData
import com.therxmv.featuremovies.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMoviesPagerFlow(): Flow<PagingData<MovieModel>>
}