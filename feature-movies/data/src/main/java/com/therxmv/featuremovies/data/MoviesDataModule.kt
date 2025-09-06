package com.therxmv.featuremovies.data

import com.therxmv.featuremovies.data.source.remote.MoviesNetworkApi
import com.therxmv.featuremovies.data.source.remote.MoviesNetworkClient
import org.koin.dsl.module

val moviesDataModule = module {
    single<MoviesNetworkApi> { MoviesNetworkClient(httpClient = get()) }
}