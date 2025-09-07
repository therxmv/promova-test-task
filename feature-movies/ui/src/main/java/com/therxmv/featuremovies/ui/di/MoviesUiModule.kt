package com.therxmv.featuremovies.ui.di

import com.therxmv.base.coroutines.KoinDispatchers
import com.therxmv.featuremovies.domain.usecase.AddFavoriteMovieUseCase
import com.therxmv.featuremovies.domain.usecase.GetFavoriteMoviesFlowUseCase
import com.therxmv.featuremovies.domain.usecase.GetMoviesPagerFlowUseCase
import com.therxmv.featuremovies.domain.usecase.RemoveFavoriteMovieUseCase
import com.therxmv.featuremovies.ui.viewmodel.MoviesViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val moviesUiModule = module {
    viewModel {
        MoviesViewModel(
            getMoviesPagerFlow = get(),
            getFavoriteMoviesFlow = get(),
            addFavoriteMovie = get(),
            removeFavoriteMovie = get(),
            connectivityObserver = get(),
            defaultDispatcher = get(named(KoinDispatchers.Default)),
            ioDispatcher = get(named(KoinDispatchers.IO)),
        )
    }

    factoryOf(::GetMoviesPagerFlowUseCase)
    factoryOf(::AddFavoriteMovieUseCase)
    factoryOf(::RemoveFavoriteMovieUseCase)
    factoryOf(::GetFavoriteMoviesFlowUseCase)
}