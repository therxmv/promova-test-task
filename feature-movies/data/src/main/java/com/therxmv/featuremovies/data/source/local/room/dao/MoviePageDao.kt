package com.therxmv.featuremovies.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.therxmv.featuremovies.data.source.local.room.entity.MoviePageEntity

@Dao
interface MoviePageDao {

    @Query("SELECT * FROM MoviePageTable WHERE movie_id = :movieId")
    suspend fun selectKeyById(movieId: Int): MoviePageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(keys: List<MoviePageEntity>)

    @Query("DELETE FROM MoviePageTable")
    suspend fun deleteKeys()
}