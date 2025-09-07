package com.therxmv.featuremovies.data.converter

import io.kotest.matchers.shouldBe
import org.junit.Test

class PosterUrlFactoryTest {

    private val systemUnderTest = PosterUrlFactory()

    @Test
    fun `it adds base url to the poster path`() {
        val path = "/path"

        val result = systemUnderTest.create(path)

        result shouldBe PosterUrlFactory.BASE_URL + path
    }
}