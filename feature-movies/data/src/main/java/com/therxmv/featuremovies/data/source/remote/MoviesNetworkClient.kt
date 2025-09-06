package com.therxmv.featuremovies.data.source.remote

import com.therxmv.featuremovies.data.BuildConfig
import com.therxmv.featuremovies.data.source.remote.dto.MoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class MoviesNetworkClient(
    private val httpClient: HttpClient,
) : MoviesNetworkApi {

    companion object { // TODO make provider if time allows
        private const val BASE_URL = "https://api.themoviedb.org"
        private const val MOVIES_PATH = "/3/discover/movie"

        private const val AUTHORIZATION = "Authorization"
        private const val API_TOKEN = "Bearer ${BuildConfig.TMDB_TOKEN}"

        private const val PAGE = "page"
        private const val SORT_BY = "sort_by"
        private const val VOTE_AVERAGE_GTE = "vote_average.gte"
        private const val VOTE_COUNT_GTE = "vote_count.gte"
    }

    override suspend fun getMoviesByPage(page: Int): MoviesResponse {
        val sortBy = "primary_release_date.desc" // TODO make filters if time allows
        val minAverageVote = 7
        val minVoteCount = 100

        return httpClient
            .get {
                url(BASE_URL + MOVIES_PATH)
                header(AUTHORIZATION, API_TOKEN)
                parameter(PAGE, page)
                parameter(SORT_BY, sortBy)
                parameter(VOTE_AVERAGE_GTE, minAverageVote)
                parameter(VOTE_COUNT_GTE, minVoteCount)
            }
            .body<MoviesResponse>()
    }
}