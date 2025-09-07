package com.therxmv.promovatest.di

import com.therxmv.base.coroutines.coroutinesModule
import com.therxmv.base.network.networkModule
import com.therxmv.featuremovies.data.di.moviesDataModule
import com.therxmv.featuremovies.ui.di.moviesUiModule
import org.koin.dsl.module

val appModules = module {
    includes(
        coroutinesModule,
        networkModule,
        navigationModule,
        moviesDataModule,
        moviesUiModule,
    )
}