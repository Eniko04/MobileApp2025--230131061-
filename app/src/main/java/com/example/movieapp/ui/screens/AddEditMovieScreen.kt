package com.example.movieapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.movieapp.data.Movie
import com.example.movieapp.ui.MovieViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMovieScreen(
    onBack: () -> Unit,
    viewModel: MovieViewModel,
    movieId: Int? = null
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf("5") }

    // 🧠 Load existing movie if editing
    LaunchedEffect(movieId) {
        if (movieId != null) {
            val movie = viewModel.getMovieById(movieId)
            movie?.let {
                title = it.title
                genre = it.genre
                selectedRating = it.rating
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (movieId == null) "Add Movie" else "Edit Movie") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 📝 Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // 🎭 Genre
            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Genre") },
                modifier = Modifier.fillMaxWidth()
            )

            // ⭐ Rating dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedRating,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rating") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    (1..10).forEach { rating ->
                        DropdownMenuItem(
                            text = { Text(rating.toString()) },
                            onClick = {
                                selectedRating = rating.toString()
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 💾 Save / Update
            Button(
                onClick = {
                    scope.launch {
                        if (movieId == null) {
                            viewModel.addMovie(
                                Movie(title = title, genre = genre, rating = selectedRating)
                            )
                        } else {
                            viewModel.updateMovie(
                                Movie(id = movieId, title = title, genre = genre, rating = selectedRating)
                            )
                        }
                        onBack()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (movieId == null) "Save Movie" else "Update Movie")
            }

            // 🗑 Delete
            if (movieId != null) {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.deleteMovie(
                                Movie(id = movieId, title = title, genre = genre, rating = selectedRating)
                            )
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Movie")
                }
            }

            // 📤 Share
            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "🎬 Check out this movie: $title ($genre) – Rating: $selectedRating/10"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share movie via"))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share Movie")
            }

            // ❌ Cancel
            OutlinedButton(
                onClick = onBack,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}
