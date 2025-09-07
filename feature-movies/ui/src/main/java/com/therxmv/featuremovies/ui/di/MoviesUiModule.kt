package com.therxmv.featuremovies.ui.di

import com.therxmv.featuremovies.domain.usecase.AddFavoriteMovieUseCase
import com.therxmv.featuremovies.domain.usecase.GetFavoriteMoviesFlowUseCase
import com.therxmv.featuremovies.domain.usecase.GetMoviesPagerFlowUseCase
import com.therxmv.featuremovies.domain.usecase.RemoveFavoriteMovieUseCase
import com.therxmv.featuremovies.ui.viewmodel.MoviesViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val moviesUiModule = module {
    viewModelOf(::MoviesViewModel)

    factoryOf(::GetMoviesPagerFlowUseCase)
    factoryOf(::AddFavoriteMovieUseCase)
    factoryOf(::RemoveFavoriteMovieUseCase)
    factoryOf(::GetFavoriteMoviesFlowUseCase)
}