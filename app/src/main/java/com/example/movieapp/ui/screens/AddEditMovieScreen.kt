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

/**
 * Екран за добавяне или редакция на филм.
 * Използва Jetpack Compose и работи заедно с MovieViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMovieScreen(
    onBack: () -> Unit,          // функция за връщане назад
    viewModel: MovieViewModel,   // връзка с ViewModel-а
    movieId: Int? = null         // ако е подаден — редакция, иначе добавяне
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Състояния за въведените данни
    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf("5") }

    /**
     * Ако сме в режим "редакция" — зареждаме данните за съответния филм.
     */
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

    // Основният layout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (movieId == null) "Добави филм" else "Редактирай филм") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
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
            // 🔤 Поле за заглавие
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заглавие") },
                modifier = Modifier.fillMaxWidth()
            )

            // 🎭 Поле за жанр
            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Жанр") },
                modifier = Modifier.fillMaxWidth()
            )

            // ⭐ Dropdown меню за избор на рейтинг (1–10)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedRating,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Рейтинг") },
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

            // 💾 Записване / Обновяване
            Button(
                onClick = {
                    scope.launch {
                        if (movieId == null) {
                            // Добавяне на нов филм
                            viewModel.addMovie(
                                Movie(title = title, genre = genre, rating = selectedRating)
                            )
                        } else {
                            // Актуализиране на съществуващ филм
                            viewModel.updateMovie(
                                Movie(id = movieId, title = title, genre = genre, rating = selectedRating)
                            )
                        }
                        onBack() // връщане към списъка
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (movieId == null) "Запиши филма" else "Обнови филма")
            }

            // 🗑 Изтриване на филм
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
                    Text("Изтрий филма")
                }
            }

            // 📤 Споделяне на филм чрез Intent
            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "🎬 Препоръчвам този филм: $title ($genre) – Оценка: $selectedRating/10"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Сподели чрез"))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сподели филма")
            }

            // ❌ Отказ (връща към предишния екран)
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Откажи")
            }
        }
    }
}
