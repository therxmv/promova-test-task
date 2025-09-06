package com.therxmv.featuremovies.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity

@Dao
interface MoviesDao {

    @Query("SELECT * FROM MoviesTable")
    suspend fun selectMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Query("DELETE FROM MoviesTable")
    suspend fun deleteMovies()
}