package com.therxmv.featuremovies.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.therxmv.featuremovies.data.converter.MovieConverter
import com.therxmv.featuremovies.data.source.local.room.MoviesDatabase
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.paging.MoviesRemoteMediator
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi
import com.therxmv.featuremovies.domain.model.MovieModel
import com.therxmv.featuremovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class MoviesRepositoryImpl(
    private val moviesNetworkApi: MoviesNetworkApi,
    private val moviesDatabase: MoviesDatabase,
    private val movieConverter: MovieConverter,
) : MoviesRepository {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private val moviesDao = moviesDatabase.getMoviesDao()

    override fun getMoviesPagerFlow(): Flow<PagingData<MovieModel>> {
        return getMoviesPager().flow.map { data ->
            data.map { movieConverter.entityToModel(it) }
        }
    }

    private fun getMoviesPager(): Pager<Int, MovieEntity> =
        Pager(
            config = PagingConfig(PAGE_SIZE),
            remoteMediator = MoviesRemoteMediator(
                moviesNetworkApi = moviesNetworkApi,
                moviesDatabase = moviesDatabase,
                movieConverter = movieConverter,
            ),
        ) {
            moviesDao.pagingSource()
        }
}