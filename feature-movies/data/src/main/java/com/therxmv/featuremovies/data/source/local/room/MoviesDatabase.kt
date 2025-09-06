package com.therxmv.featuremovies.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.therxmv.featuremovies.data.source.local.room.dao.MoviePageDao
import com.therxmv.featuremovies.data.source.local.room.dao.MoviesDao
import com.therxmv.featuremovies.data.source.local.room.entity.MovieEntity
import com.therxmv.featuremovies.data.source.local.room.entity.MoviePageEntity

internal const val MOVIES_DB_NAME = "movies_database.db"

@Database(version = 1, entities = [MovieEntity::class, MoviePageEntity::class])
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
    abstract fun getMoviePageDao(): MoviePageDao
}