package com.example.movieapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val genre: String,
    val rating: String,
    val isFavorite: Boolean = false
)
