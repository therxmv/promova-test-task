package com.therxmv.featuremovies.data.source.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity

@Dao
interface MoviesDao {

    @Query("SELECT * FROM MoviesTable ORDER BY release_date_millis DESC")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM MoviesTable WHERE id IN (:ids) ORDER BY release_date_millis DESC")
    suspend fun selectMoviesByIds(ids: List<Int>): List<MovieEntity>

    @Query("SELECT * FROM MoviesTable")
    suspend fun selectMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM MoviesTable")
    suspend fun deleteMovies()

    @Query("SELECT COUNT(*) FROM MoviesTable")
    suspend fun getCount(): Int
}