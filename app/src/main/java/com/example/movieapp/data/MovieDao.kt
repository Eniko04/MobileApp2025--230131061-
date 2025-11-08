package com.example.movieapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie ORDER BY id DESC")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("UPDATE movie SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFav: Boolean)
}
