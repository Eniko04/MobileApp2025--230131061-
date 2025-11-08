package com.example.movieapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.data.Movie
import com.example.movieapp.ui.MovieViewModel

/**
 * Основен екран – показва всички филми от базата.
 * Оттук може да се добавя нов филм, да се редактира съществуващ
 * или да се отбележи като "любим".
 */
@Composable
fun MovieListScreen(
    onAddClick: () -> Unit,          // действие при натискане на бутона "+"
    onMovieClick: (Int) -> Unit,     // отваря екрана за конкретен филм
    viewModel: MovieViewModel        // достъп до данните чрез ViewModel
) {
    // Слушаме потока от филми (Flow) и го превръщаме в State
    val movies by viewModel.movies.collectAsState()

    Scaffold(
        floatingActionButton = {
            // Плаващ бутон за добавяне на нов филм
            ExtendedFloatingActionButton(
                text = { Text("Add") },
                icon = { Text("＋") },
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // 🕳️ Ако няма филми – показваме съобщение
            if (movies.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎞️ No movies yet.\n" +
                                "Press + to add!",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                // 📜 Изброяваме всеки филм от базата
                movies.forEach { movie ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onMovieClick(movie.id) } // натискане -> отваря детайли
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // 🎬 Информация за филма
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = movie.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Genre: ${movie.genre}",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "⭐ Raiting: ${movie.rating}/10",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                                )
                            }

                            // ⭐ Бутона за добавяне в "любими"
                            IconButton(onClick = { viewModel.toggleFavorite(movie) }) {
                                Icon(
                                    imageVector = if (movie.isFavorite)
                                        Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Favorite",
                                    tint = if (movie.isFavorite)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
