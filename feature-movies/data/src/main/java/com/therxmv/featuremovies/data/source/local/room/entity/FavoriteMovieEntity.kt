package com.therxmv.featuremovies.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteMovieTable")
data class FavoriteMovieEntity(
    @PrimaryKey @ColumnInfo(name = "movie_id") val movieId: Int,
)