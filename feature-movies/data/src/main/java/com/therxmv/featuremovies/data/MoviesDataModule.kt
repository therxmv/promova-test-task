package com.therxmv.featuremovies.data

import android.content.Context
import androidx.room.Room
import com.therxmv.featuremovies.data.source.local.room.MOVIES_DB_NAME
import com.therxmv.featuremovies.data.source.local.room.MoviesDatabase
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkClient
import org.koin.dsl.module

val moviesDataModule = module {
    single<MoviesNetworkApi> { MoviesNetworkClient(httpClient = get()) }

    single<MoviesDatabase> {
        Room.databaseBuilder(
            context = get<Context>().applicationContext,
            klass = MoviesDatabase::class.java,
            name = MOVIES_DB_NAME,
        ).build()
    }
}