package com.therxmv.featuremovies.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.therxmv.featuremovies.data.source.local.room.entity.FavoriteMovieEntity
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMoviesDao {

    @Query("SELECT m.* FROM MoviesTable m INNER JOIN FavoriteMovieTable f ON f.movie_id = m.id ORDER BY m.release_date_millis DESC")
    fun selectFavoriteMoviesEntities(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(entry: FavoriteMovieEntity)

    @Query("DELETE FROM FavoriteMovieTable WHERE movie_id = :movieId")
    suspend fun deleteFavoriteMovie(movieId: Int)
}