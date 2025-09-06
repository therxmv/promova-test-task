package com.therxmv.featuremovies.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MoviePageTable")
data class MoviePageEntity(
    @PrimaryKey @ColumnInfo("movie_id") val movieId: Int,
    @ColumnInfo("prev_page") val prevPage: Int?,
    @ColumnInfo("next_page") val nextPage: Int?,
)