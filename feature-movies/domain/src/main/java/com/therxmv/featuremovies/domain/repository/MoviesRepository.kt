package com.therxmv.featuremovies.domain.repository

import androidx.paging.PagingData
import com.therxmv.featuremovies.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMoviesPagerFlow(): Flow<PagingData<MovieModel>>

    fun getFavoriteMoviesFlow(): Flow<List<MovieModel>>

    suspend fun addMovieToFavorites(id: Int)

    suspend fun removeMovieFromFavorites(id: Int)
}