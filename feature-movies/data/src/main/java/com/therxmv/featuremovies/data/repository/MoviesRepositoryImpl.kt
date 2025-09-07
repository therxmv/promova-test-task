package com.therxmv.featuremovies.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.therxmv.featuremovies.data.converter.MovieConverter
import com.therxmv.featuremovies.data.source.local.room.MoviesDatabase
import com.therxmv.featuremovies.data.source.local.room.entity.FavoriteMovieEntity
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.paging.MoviesRemoteMediator
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi
import com.therxmv.featuremovies.domain.model.MovieModel
import com.therxmv.featuremovies.domain.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

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
    private val favoriteMoviesDao = moviesDatabase.getFavoriteMoviesDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMoviesPagerFlow(): Flow<PagingData<MovieModel>> =
        getMoviesPager().flow.map { data ->
            data.map { entity ->
                movieConverter.entityToModel(entity = entity)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavoriteMoviesFlow(): Flow<List<MovieModel>> =
        favoriteMoviesDao.selectFavoriteMovies()
            .mapLatest { list ->
                val ids = list.map { it.movieId }

                moviesDao.selectMoviesByIds(ids).map { entity ->
                    movieConverter.entityToModel(entity = entity, isFavorite = true)
                }
            }

    override suspend fun addMovieToFavorites(id: Int) {
        favoriteMoviesDao.insertFavoriteMovie(FavoriteMovieEntity(id))
    }

    override suspend fun removeMovieFromFavorites(id: Int) {
        favoriteMoviesDao.deleteFavoriteMovie(id)
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