package com.example.movieapp.data

class MovieRepository(private val dao: MovieDao) {
    val movies = dao.getAllMovies()

    suspend fun addMovie(movie: Movie) = dao.insertMovie(movie)
    suspend fun updateMovie(movie: Movie) = dao.updateMovie(movie)
    suspend fun deleteMovie(movie: Movie) = dao.deleteMovie(movie)
    suspend fun getMovieById(id: Int) = dao.getMovieById(id)
}
