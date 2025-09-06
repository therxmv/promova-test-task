package com.therxmv.featuremovies.data.source.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.therxmv.featuremovies.data.converter.MovieConverter
import com.therxmv.featuremovies.data.source.local.room.MoviesDatabase
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val moviesNetworkApi: MoviesNetworkApi,
    private val moviesDatabase: MoviesDatabase,
    private val movieConverter: MovieConverter,
) : RemoteMediator<Int, MovieEntity>() {

    companion object {
        private const val INITIAL_PAGE = 1
    }

    private val moviesDao = moviesDatabase.getMoviesDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult =
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> INITIAL_PAGE

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> state.resolveNextPage()
            }

            val response = moviesNetworkApi.getMoviesByPage(page)

            val entities = movieConverter.dtoToEntity(response.results)

            moviesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    moviesDao.deleteMovies()
                }

                moviesDao.insertMovies(entities)
            }

            MediatorResult.Success(endOfPaginationReached = response.page >= response.totalPages)
        } catch (e: Exception) {
            e.printStackTrace()
            e.fallbackToLocalOrReturnError()
        }

    private suspend fun Exception.fallbackToLocalOrReturnError(): MediatorResult =
        if (moviesDao.getCount() > 0) {
            MediatorResult.Success(endOfPaginationReached = true) // TODO maybe false
        } else {
            MediatorResult.Error(this)
        }

    private fun PagingState<Int, MovieEntity>.resolveNextPage(): Int =
        anchorPosition?.let { anchor ->
            val page = closestPageToPosition(anchor)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        } ?: INITIAL_PAGE.plus(1)
}