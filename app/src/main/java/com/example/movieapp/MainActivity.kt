package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.ui.screens.AddEditMovieScreen
import com.example.movieapp.ui.screens.MovieListScreen
import com.example.movieapp.ui.theme.MovieAppTheme

/**
 * Главната активност (MainActivity) – стартира Jetpack Compose интерфейса,
 * създава ViewModel и управлява навигацията между екраните.
 */
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // 🌙 Превключване между тъмна и светла тема
            var darkMode by remember { mutableStateOf(false) }

            // 🧭 Контролер за навигация
            val navController = rememberNavController()

            // 🎬 ViewModel – осигурява достъп до базата (Room)
            val viewModel: MovieViewModel = viewModel(
                factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
                    .getInstance(application)
            )

            // 🎨 Обвивка с темата на приложението
            MovieAppTheme(darkTheme = darkMode) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("🎬 MovieApp") },
                            actions = {
                                // 🌗 Бутон за смяна на тема
                                IconButton(onClick = { darkMode = !darkMode }) {
                                    Icon(
                                        imageVector = if (darkMode)
                                            Icons.Default.LightMode
                                        else
                                            Icons.Default.DarkMode,
                                        contentDescription = "Превключи тема"
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->

                    /**
                     * 🧭 Навигационна графика (Compose Navigation)
                     * Определя трите основни маршрута в приложението:
                     * - "list" → Списък с филми
                     * - "add" → Добавяне на нов филм
                     * - "edit/{id}" → Редакция на избран филм
                     */
                    NavHost(
                        navController = navController,
                        startDestination = "list",
                        modifier = Modifier.padding(padding)
                    ) {

                        // 🏠 Екран със списък от филми
                        composable("list") {
                            MovieListScreen(
                                onAddClick = { navController.navigate("add") },
                                onMovieClick = { movieId ->
                                    navController.navigate("edit/$movieId")
                                },
                                viewModel = viewModel
                            )
                        }

                        // ➕ Екран за добавяне на нов филм
                        composable("add") {
                            AddEditMovieScreen(
                                onBack = { navController.popBackStack() },
                                viewModel = viewModel
                            )
                        }

                        // ✏️ Екран за редактиране на съществуващ филм
                        composable("edit/{id}") { backStackEntry ->
                            val movieId = backStackEntry.arguments
                                ?.getString("id")
                                ?.toIntOrNull()
                            AddEditMovieScreen(
                                onBack = { navController.popBackStack() },
                                movieId = movieId,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
