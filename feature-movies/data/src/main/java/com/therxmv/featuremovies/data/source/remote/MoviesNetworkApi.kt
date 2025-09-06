package com.therxmv.featuremovies.data.source.remote

import com.therxmv.featuremovies.data.source.remote.dto.MoviesResponse

interface MoviesNetworkApi {

    suspend fun getMoviesByPage(page: Int): MoviesResponse
}