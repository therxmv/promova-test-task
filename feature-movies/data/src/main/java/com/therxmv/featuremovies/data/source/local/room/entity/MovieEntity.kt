package com.therxmv.featuremovies.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MoviesTable")
data class MovieEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("overview") val overview: String,
    @ColumnInfo("poster_path") val posterPath: String,
    @ColumnInfo("release_date_millis") val releaseDateMillis: Long,
    @ColumnInfo("average_vote") val averageVote: Float,
)