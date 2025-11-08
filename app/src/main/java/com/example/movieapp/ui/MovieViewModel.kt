package com.example.movieapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.AppDatabase
import com.example.movieapp.data.Movie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel класът отговаря за управлението на данните и логиката между UI и базата данни (Room).
 * Работи с корутини, за да не блокира основния (UI) поток.
 */
class MovieViewModel(application: Application) : AndroidViewModel(application) {

    // 🔗 Връзка с базата данни чрез DAO
    private val dao = AppDatabase.getDatabase(application).movieDao()

    /**
     * Поток (Flow) от всички филми в базата.
     * Използваме stateIn, за да се преобразува в StateFlow,
     * което Compose може да наблюдава директно.
     */
    val movies = dao.getAllMovies()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Добавя нов филм в базата данни.
     * Изпълнява се в корутина, за да не блокира интерфейса.
     */
    fun addMovie(movie: Movie) = viewModelScope.launch {
        dao.insertMovie(movie)
    }

    /**
     * Обновява информацията за съществуващ филм.
     */
    fun updateMovie(movie: Movie) = viewModelScope.launch {
        dao.updateMovie(movie)
    }

    /**
     * Изтрива филм от базата.
     */
    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        dao.deleteMovie(movie)
    }

    /**
     * Превключва флага дали филмът е "любим".
     * Ако вече е favorite → го премахва, иначе го добавя.
     */
    fun toggleFavorite(movie: Movie) = viewModelScope.launch {
        dao.updateFavorite(movie.id, !movie.isFavorite)
    }

    /**
     * Връща конкретен филм по ID от текущия списък.
     * Тук използваме локалното копие (StateFlow),
     * за да избегнем нова заявка към базата.
     */
    suspend fun getMovieById(id: Int): Movie? {
        return movies.value.find { it.id == id }
    }
}
