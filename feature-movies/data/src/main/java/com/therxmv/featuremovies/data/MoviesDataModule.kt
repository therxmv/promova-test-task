package com.therxmv.featuremovies.data

import android.content.Context
import androidx.room.Room
import com.therxmv.featuremovies.data.converter.MovieConverter
import com.therxmv.featuremovies.data.converter.PosterUrlFactory
import com.therxmv.featuremovies.data.repository.MoviesRepositoryImpl
import com.therxmv.featuremovies.data.source.local.room.MOVIES_DB_NAME
import com.therxmv.featuremovies.data.source.local.room.MoviesDatabase
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkClient
import com.therxmv.featuremovies.domain.repository.MoviesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val moviesDataModule = module {
    singleOf(::PosterUrlFactory)
    singleOf(::MovieConverter)

    single<MoviesNetworkApi> { MoviesNetworkClient(httpClient = get()) }

    single<MoviesDatabase> {
        Room.databaseBuilder(
            context = get<Context>().applicationContext,
            klass = MoviesDatabase::class.java,
            name = MOVIES_DB_NAME,
        ).build()
    }

    single<MoviesRepository> {
        MoviesRepositoryImpl(
            moviesNetworkApi = get(),
            moviesDatabase = get(),
            movieConverter = get(),
        )
    }
}