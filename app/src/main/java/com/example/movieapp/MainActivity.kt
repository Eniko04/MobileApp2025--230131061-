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

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // 🌙 Light/Dark Mode state
            var darkMode by remember { mutableStateOf(false) }

            // 🚀 Navigation controller
            val navController = rememberNavController()

            // 🎬 ViewModel (Room + Repository)
            val viewModel: MovieViewModel = viewModel(
                factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )

            // 🎨 Theme wrapper
            MovieAppTheme(darkTheme = darkMode) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("🎬 MovieApp") },
                            actions = {
                                // 🌗 Theme toggle
                                IconButton(onClick = { darkMode = !darkMode }) {
                                    Icon(
                                        imageVector = if (darkMode)
                                            Icons.Default.LightMode
                                        else
                                            Icons.Default.DarkMode,
                                        contentDescription = "Toggle theme"
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->

                    // 🧭 Navigation graph
                    NavHost(
                        navController = navController,
                        startDestination = "list",
                        modifier = Modifier.padding(padding)
                    ) {

                        // 🏠 Movie List Screen
                        composable("list") {
                            MovieListScreen(
                                onAddClick = { navController.navigate("add") },
                                onMovieClick = { movieId ->
                                    navController.navigate("edit/$movieId")
                                },
                                viewModel = viewModel
                            )
                        }

                        // ➕ Add Movie Screen
                        composable("add") {
                            AddEditMovieScreen(
                                onBack = { navController.popBackStack() },
                                viewModel = viewModel
                            )
                        }

                        // ✏️ Edit Movie Screen
                        composable("edit/{id}") { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
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
