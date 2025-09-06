package com.therxmv.featuremovies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.therxmv.featuremovies.domain.usecase.GetMoviesPagerFlowUseCase

class MoviesViewModel(
    getMoviesPagerFlowUseCase: GetMoviesPagerFlowUseCase,
) : ViewModel() {

    val moviesFlow = getMoviesPagerFlowUseCase().cachedIn(viewModelScope)
}