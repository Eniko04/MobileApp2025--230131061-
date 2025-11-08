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

@Composable
fun MovieListScreen(
    onAddClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: MovieViewModel
) {
    val movies by viewModel.movies.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Movie") },
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
            if (movies.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎞️ No movies yet.\nTap + to start your list!",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                movies.forEach { movie ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onMovieClick(movie.id) }
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
                                    text = "⭐ Rating: ${movie.rating}/10",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                                )
                            }
                            IconButton(onClick = { viewModel.toggleFavorite(movie) }) {
                                Icon(
                                    imageVector = if (movie.isFavorite)
                                        Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Favorite",
                                    tint = if (movie.isFavorite)
                                        MaterialTheme.colorScheme.primary else
                                        MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
