package com.therxmv.featuremovies.data.converter

class PosterUrlFactory {

    companion object {
        internal const val BASE_URL = "https://image.tmdb.org/t/p/w440_and_h660_face"
    }

    fun create(path: String): String =
        BASE_URL + path
}