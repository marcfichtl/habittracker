package com.example.habittracker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screens(val route: String) {
    Main("Main"),
    Add("Add"),
    Edit("edit/{habitId}")
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    dataViewModel: DataViewModel = viewModel(factory = ViewModelFactoryProvider.Factory)
){
    val navController = rememberNavController()
    val state by dataViewModel.habitsUiState.collectAsStateWithLifecycle()

    NavHost(navController, Screens.Main.route,
        modifier = modifier
    ){
        composable(Screens.Main.route) {
            TODO("Display the Habits on a List")
        }
        composable(Screens.Add.route) {
            TODO("Add a new Habit")
        }
        composable(Screens.Edit.route) {
            TODO("Edit a Habit")
        }
    }
}