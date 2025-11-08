package com.example.movieapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) интерфейсът, чрез който достъпваме таблицата с филми в базата данни.
 * Тук са описани всички основни CRUD операции (Create, Read, Update, Delete).
 */
@Dao
interface MovieDao {

    /**
     * Връща списък с всички филми, подредени по id (най-новите първи).
     * Използва се Flow, за да може UI-ът автоматично да се обновява при промени.
     */
    @Query("SELECT * FROM movie ORDER BY id DESC")
    fun getAllMovies(): Flow<List<Movie>>

    /**
     * Взима конкретен филм по неговото ID.
     * Ако няма такъв, връща null.
     */
    @Query("SELECT * FROM movie WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id: Int): Movie?

    /**
     * Добавя нов филм в базата.
     * Ако вече съществува запис със същото ID, той се презаписва.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    /**
     * Обновява данните за даден филм.
     * Полезно при редакция на заглавие, жанр и др.
     */
    @Update
    suspend fun updateMovie(movie: Movie)

    /**
     * Изтрива избрания филм от базата.
     */
    @Delete
    suspend fun deleteMovie(movie: Movie)

    /**
     * Променя флага дали филмът е в любими.
     * Използва се, когато потребителят натисне бутона "Favorite" в приложението.
     */
    @Query("UPDATE movie SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFav: Boolean)
}
