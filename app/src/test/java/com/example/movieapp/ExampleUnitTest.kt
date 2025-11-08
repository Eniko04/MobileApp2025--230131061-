package com.example.movieapp

import com.example.movieapp.data.Movie
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `favorite toggles correctly`() {
        val movie = Movie(1, "Inception", "Sci-Fi", "9", false)
        val updated = movie.copy(isFavorite = !movie.isFavorite)
        assertTrue(updated.isFavorite)
    }

    @Test
    fun `rating should not be empty`() {
        val movie = Movie(2, "Avatar", "Fantasy", "8", false)
        assertTrue(movie.rating.isNotEmpty())
    }
}
