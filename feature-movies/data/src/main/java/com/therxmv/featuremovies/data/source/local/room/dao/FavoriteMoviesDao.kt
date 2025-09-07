package com.therxmv.featuremovies.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.therxmv.featuremovies.data.source.local.room.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMoviesDao {

    @Query("SELECT * FROM FavoriteMovieTable")
    fun selectFavoriteMovies(): Flow<List<FavoriteMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(entry: FavoriteMovieEntity)

    @Query("DELETE FROM FavoriteMovieTable WHERE movie_id = :movieId")
    suspend fun deleteFavoriteMovie(movieId: Int)
}