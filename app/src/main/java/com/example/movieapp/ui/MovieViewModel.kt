package com.example.movieapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.AppDatabase
import com.example.movieapp.data.Movie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).movieDao()

    val movies = dao.getAllMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMovie(movie: Movie) = viewModelScope.launch {
        dao.insertMovie(movie)
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch {
        dao.updateMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        dao.deleteMovie(movie)
    }

    fun toggleFavorite(movie: Movie) = viewModelScope.launch {
        dao.updateFavorite(movie.id, !movie.isFavorite)
    }

    suspend fun getMovieById(id: Int): Movie? {
        return movies.value.find { it.id == id }
    }
}
